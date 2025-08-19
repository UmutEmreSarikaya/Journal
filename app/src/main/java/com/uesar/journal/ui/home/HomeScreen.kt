package com.uesar.journal.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.items
import com.uesar.journal.R
import com.uesar.journal.ui.ObserveAsEvents
import com.uesar.journal.ui.home.components.AudioRecordingBottomSheet
import com.uesar.journal.ui.home.components.JournalEntryRow
import com.uesar.journal.ui.home.components.NoEntries
import com.uesar.journal.ui.theme.standardPadding
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToNewEntry: (String) -> Unit,
    navigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is HomeEvent.AudioRecorded -> {
                navigateToNewEntry(event.audioPath)
            }
        }
    }

    HomeScreen(
        state = state,

        onAction = { action ->
            when (action) {

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
                .padding(standardPadding)
                .fillMaxSize(),
            verticalArrangement = if (state.journalEntries.isEmpty()) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.journalEntries.isEmpty()) {
                NoEntries()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(standardPadding)) {
                    items(state.journalEntries) { entry ->
                        JournalEntryRow(
                            journalEntry = entry,
                            startPlaying = {onAction(HomeAction.StartPlaying(entry.audioPath))},
                            resumePlaying = {onAction(HomeAction.ResumePlaying)},
                            pausePlaying = {onAction(HomeAction.PausePlaying)},
                            currentTime = entry.currentTime,
                            totalTime = entry.totalTime,
                            playerState = entry.playerState
                        )
                    }
                }
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
                            }
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(HomeState(), onAction = {})
}
