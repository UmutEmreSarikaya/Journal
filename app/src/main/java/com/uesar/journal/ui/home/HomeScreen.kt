package com.uesar.journal.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.ObserveAsEvents
import com.uesar.journal.ui.home.components.AudioRecordingBottomSheet
import com.uesar.journal.ui.home.components.FilterPopup
import com.uesar.journal.ui.home.components.JournalEntryRow
import com.uesar.journal.ui.home.components.NoEntries
import com.uesar.journal.ui.home.components.SelectedFilterRow
import com.uesar.journal.ui.theme.OnPrimary
import com.uesar.journal.ui.theme.PrimaryContainer
import com.uesar.journal.ui.theme.smallPadding
import com.uesar.journal.ui.theme.standardPadding
import com.uesar.journal.ui.utils.Utils.groupEntriesByDate
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
            Text(stringResource(R.string.your_journal))
        }, actions = {
            IconButton(onClick = { onAction(HomeAction.SettingsClicked) }) {
                Icon(painter = painterResource(R.drawable.settings), contentDescription = null)
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(
            containerColor = PrimaryContainer,
            contentColor = OnPrimary,
            onClick = {
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
                .padding(horizontal = standardPadding)
                .fillMaxSize(),
            verticalArrangement = if (state.journalEntries.none { it.isShown }) Arrangement.Center else Arrangement.Top,
        ) {
            FilterChip(
                modifier = Modifier
                    .height(32.dp)
                    .animateContentSize(),
                selected = false,
                onClick = { onAction(HomeAction.FilterChipClicked) },
                label = {
                    SelectedFilterRow(state.selectedFilters, { onAction(HomeAction.ClearFilter) })
                })
            if (state.isFilterPopupOpen) {
                FilterPopup(
                    onDismissRequest = { onAction(HomeAction.DismissFilterPopup) },
                    onItemClicked = { onAction(HomeAction.FilterItemClicked(it)) },
                    filterList = state.selectedFilters
                )
            }
            if (state.journalEntries.none { it.isShown }) {
                NoEntries(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = smallPadding)) {
                    groupEntriesByDate(state.journalEntries.filter { it.isShown }).forEach { (dateTitle, entriesForDate) ->
                        item {
                            Text(
                                text = dateTitle,
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        itemsIndexed(entriesForDate) { index, entry ->
                            Column(modifier = Modifier.padding(top = smallPadding)) {
                                if (entry.isShown) {
                                    JournalEntryRow(
                                        modifier = Modifier.padding(vertical = smallPadding),
                                        journalEntry = entry,
                                        isLastEntryInGroup = entriesForDate.lastIndex == index,
                                        startPlaying = { onAction(HomeAction.StartPlaying(entry.audioPath)) },
                                        resumePlaying = { onAction(HomeAction.ResumePlaying) },
                                        pausePlaying = { onAction(HomeAction.PausePlaying) },
                                        currentTime = entry.currentTime,
                                        totalTime = entry.totalTime,
                                        playerState = entry.playerState
                                    )
                                }
                            }
                        }
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
