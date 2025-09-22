package com.uesar.journal.ui.newentry

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.components.AudioPlayerUI
import com.uesar.journal.ui.newentry.components.MoodSelectorBottomSheet
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.OnPrimary
import com.uesar.journal.ui.theme.OutlineVariant
import com.uesar.journal.ui.theme.Primary
import com.uesar.journal.ui.theme.PrimaryContainer
import com.uesar.journal.ui.theme.smallPadding
import com.uesar.journal.ui.theme.standardPadding
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewEntryScreenRoot(
    viewModel: NewEntryViewModel = koinViewModel(), audioPath: String, navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.setAudioPath(audioPath)
    }

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
            Text(stringResource(R.string.new_entry))
        }, navigationIcon = {
            IconButton(onClick = { onAction(NewEntryAction.OpenNavigationDialog) }) {
                Icon(
                    painter = painterResource(R.drawable.navigate_before), contentDescription = null
                )
            }
        })
    }) { innerPadding ->
        BackHandler {
            onAction(NewEntryAction.OpenNavigationDialog)
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = standardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onAction(NewEntryAction.OnChangeMoodClicked) },
                    painter = painterResource(
                        state.journalEntryUIState.mood?.icon ?: R.drawable.change_mood
                    ),
                    contentDescription = null
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.journalEntryUIState.title,
                    onValueChange = { onAction(NewEntryAction.OnTitleChanged(it)) },
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.add_title)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = OutlineVariant,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            AudioPlayerUI(
                startPlaying = { onAction(NewEntryAction.StartPlaying) },
                resumePlaying = { onAction(NewEntryAction.ResumePlaying) },
                pausePlaying = { onAction(NewEntryAction.PausePlaying) },
                currentTime = state.journalEntryUIState.currentTime,
                totalTime = state.journalEntryUIState.totalTime,
                mood = state.journalEntryUIState.mood,
                playerState = state.journalEntryUIState.playerState
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = state.journalEntryUIState.description,
                onValueChange = { onAction(NewEntryAction.OnDescriptionChanged(it)) },
                placeholder = { Text(stringResource(R.string.add_description)) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = OutlineVariant,
                    focusedPlaceholderColor = OutlineVariant,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = standardPadding),
                horizontalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Button(
                    modifier = Modifier.weight(1.5F), colors = ButtonDefaults.buttonColors(
                        containerColor = InverseOnSurface, contentColor = Primary
                    ), onClick = { onAction(NewEntryAction.OpenNavigationDialog) }) {
                    Text(
                        stringResource(R.string.cancel)
                    )
                }
                Button(
                    modifier = Modifier.weight(3F),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryContainer, contentColor = OnPrimary
                    ),
                    onClick = {
                        onAction(NewEntryAction.SaveEntry)
                        onAction(NewEntryAction.NavigateBack)
                    },
                    enabled = state.journalEntryUIState.mood != null && state.journalEntryUIState.title.isNotEmpty()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.padding(end = smallPadding),
                            painter = painterResource(R.drawable.check),
                            contentDescription = null
                        )
                        Text(stringResource(R.string.confirm))
                    }
                }
            }

            if (state.isBottomSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState, onDismissRequest = {
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
                                    NewEntryAction.OnConfirmMoodClicked(mood)
                                )
                            }
                        },
                        onMoodIconClicked = { mood -> onAction(NewEntryAction.OnMoodIconClicked(mood)) })
                }
            }
        }
    }

    if (state.isNavigationDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(NewEntryAction.CloseNavigationDialog) },
            title = { Text(stringResource(R.string.are_you_sure)) },
            text = { Text(stringResource(R.string.do_you_want_to_go_back)) },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = InverseOnSurface),
                    onClick = {
                        onAction(NewEntryAction.CloseNavigationDialog)
                        onAction(NewEntryAction.StopPlaying)
                        onAction(NewEntryAction.NavigateBack)
                        onAction(NewEntryAction.DeleteAudioFile(state.journalEntryUIState.audioPath))
                    }) {
                    Text(color = Primary, text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                    onClick = {
                        onAction(NewEntryAction.CloseNavigationDialog)
                    }) {
                    Text(color = OnPrimary, text = stringResource(R.string.no))
                }
            })
    }
}

@Preview
@Composable
private fun NewEntryScreenPreview() {
    NewEntryScreen(state = NewEntryState(), onAction = {})
}
