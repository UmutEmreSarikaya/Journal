package com.uesar.journal.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.theme.ButtonGradient
import com.uesar.journal.ui.theme.ErrorContainer
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.OnErrorContainer
import com.uesar.journal.ui.theme.Primary
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
                    BottomSheetContent(
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

@Composable
private fun BottomSheetContent(
    timePassed: String,
    isRecording: Boolean,
    onCancelButtonClicked: () -> Unit,
    onResumeButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isRecording) "Recording your memories..." else "Recording Paused")
        Text(timePassed)
        Row(
            horizontalArrangement = Arrangement.spacedBy(71.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 51.dp, bottom = 25.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ErrorContainer)
                    .clickable { onCancelButtonClicked() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = null,
                    tint = OnErrorContainer
                )
            }
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(ButtonGradient)
                    .clickable(
                        onClick = { if (isRecording) onSaveButtonClicked() else onResumeButtonClicked() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = if (isRecording) painterResource(R.drawable.check) else painterResource(
                        R.drawable.mic
                    ),
                    contentDescription = if (isRecording) "Finish Recording" else "Start Recording",
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(InverseOnSurface)
                    .clickable { if (isRecording) onPauseButtonClicked() else onSaveButtonClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isRecording) painterResource(R.drawable.pause) else painterResource(
                        R.drawable.check
                    ), contentDescription = null, tint = Primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(HomeState(), onAction = {})
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent("00:00:00", true, {}, {}, {}, {})
}
