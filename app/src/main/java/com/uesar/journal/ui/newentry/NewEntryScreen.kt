package com.uesar.journal.ui.newentry

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.newentry.components.MoodSelectorBottomSheet
import com.uesar.journal.ui.theme.standardPadding
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable

fun NewEntryScreenRoot(
    viewModel: NewEntryViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    NewEntryScreen(
        state = state, onAction = { action ->
            when (action) {
                is NewEntryAction.NavigateBack -> {
                    navigateBack()
                }

                else -> Unit
            }
            viewModel.onAction(action)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewEntryScreen(
    state: NewEntryState, onAction: (NewEntryAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text("New Entry")
        }, navigationIcon = {
            IconButton(onClick = { onAction(NewEntryAction.NavigateBack) }) {
                Icon(
                    painter = painterResource(R.drawable.navigate_before),
                    contentDescription = null
                )
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = standardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onAction(NewEntryAction.OnChangeMoodClicked) },
                    painter = painterResource(state.selectedMood?.icon ?: R.drawable.change_mood),
                    contentDescription = null
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    onValueChange = { onAction(NewEntryAction.OnTitleChanged(it)) },
                    placeholder = { Text("Add Title...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                )
                IconButton(
                    modifier = Modifier,
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ai),
                        contentDescription = null,
                    )
                }
            }

            if (state.isBottomSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        onAction(NewEntryAction.BottomSheetClosed)
                        onAction(NewEntryAction.OnCancelMoodBottomSheet)
                    }) {
                    MoodSelectorBottomSheet(
                        selectedMood = state.currentSelectedMoodInBottomSheet,
                        onCancelButtonClicked = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onAction(NewEntryAction.BottomSheetClosed)
                                onAction(NewEntryAction.OnCancelMoodBottomSheet)
                            }
                        },
                        onConfirmButtonClicked = { mood ->
                            coroutineScope.launch {
                                sheetState.hide()
                                onAction(NewEntryAction.BottomSheetClosed)
                                onAction(
                                    NewEntryAction.OnConfirmClicked(mood)
                                )
                            }
                        },
                        onMoodIconClicked = { mood -> onAction(NewEntryAction.OnMoodIconClicked(mood)) })
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewEntryScreenPreview() {
    NewEntryScreen(state = NewEntryState(), onAction = {})
}
