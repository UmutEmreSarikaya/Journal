package com.uesar.journal.ui.newentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.standardPadding
import org.koin.androidx.compose.koinViewModel

@Composable

fun NewEntryScreenRoot(
    viewModel: NewEntryViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    NewEntryScreen(
        state = state, onAction = { action ->
            when(action){
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
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text("New Entry")
        }, navigationIcon = {
            IconButton(onClick = { onAction(NewEntryAction.NavigateBack) }) {
                Icon(painter = painterResource(R.drawable.navigate_before), contentDescription = null)
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = standardPadding)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(InverseOnSurface),
                    onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        modifier = Modifier.size(16.dp),
                        contentDescription = null
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    onValueChange = { onAction(NewEntryAction.OnTitleChanged(it))},
                    placeholder = { Text("Add Title...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewEntryScreenPreview() {
    NewEntryScreen(state = NewEntryState(), onAction = {})
}
