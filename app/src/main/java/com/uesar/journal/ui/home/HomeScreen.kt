package com.uesar.journal.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.home.components.AudioRecordingBottomSheet
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToNewEntry: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,

        onAction = { action ->
            when (action) {
                is HomeAction.NavigateToNewEntry -> {
                    navigateToNewEntry()
                }

                is HomeAction.SettingsClicked -> {
                    navigateToSettings()
                }

                else -> Unit
            }
            viewModel.onAction(action)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text("Your EchoJournal")
        }, actions = {
            IconButton(onClick = { onAction(HomeAction.SettingsClicked) }) {
                Icon(painter = painterResource(R.drawable.settings), contentDescription = null)
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            onAction(HomeAction.BottomSheetOpened)
            onAction(HomeAction.StartRecording)
        }) {
            Icon(
                painter = painterResource(R.drawable.add), contentDescription = null
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.entryCount == 0) {
                NoEntriesScreen()
            } else {
                Text("Entries: ${state.entryCount}")
            }
            if (state.isBottomSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        onAction(HomeAction.BottomSheetClosed)
                        onAction(HomeAction.CancelRecording)
                    }) {
                    AudioRecordingBottomSheet(
                        timePassed = state.recordingTime,
                        isRecording = state.isRecording,
                        onResumeButtonClicked = { onAction(HomeAction.ResumeRecording) },
                        onCancelButtonClicked = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onAction(HomeAction.BottomSheetClosed)
                                onAction(HomeAction.CancelRecording)
                            }
                        },
                        onPauseButtonClicked = { onAction(HomeAction.PauseRecording) },
                        onSaveButtonClicked = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onAction(HomeAction.BottomSheetClosed)
                                onAction(HomeAction.SaveRecording)
                                onAction(HomeAction.NavigateToNewEntry)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NoEntriesScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_entry), contentDescription = null
        )
        Text(modifier = Modifier.padding(top = 34.dp), text = "No Entries")
        Text("Start recording your first Echo")
    }
}



@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(HomeState(), onAction = {})
}
