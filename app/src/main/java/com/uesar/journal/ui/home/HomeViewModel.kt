package com.uesar.journal.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uesar.journal.AudioPlayer
import com.uesar.journal.AudioRecorder
import com.uesar.journal.domain.JournalRepository
import com.uesar.journal.ui.home.HomeEvent.*
import com.uesar.journal.ui.model.mapper.toUIState
import com.uesar.journal.ui.utils.formatSecondsToMinutes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.Locale

class HomeViewModel(
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer,
    private val application: Application,
    repository: JournalRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        audioRecorder.recordingTime.onEach { recordingTime ->
            _state.update { it.copy(recordingTime = formatCounter(recordingTime)) }
        }.launchIn(viewModelScope)

        repository.getJournalEntries().onEach { entries ->
            _state.update { it.copy(journalEntries = entries.map { it.toUIState() }) }
            _state.update { state ->
                state.copy(
                    journalEntries = state.journalEntries.mapIndexed { index, journalEntry ->
                        journalEntry.copy(
                            totalTime = formatSecondsToMinutes(
                                audioPlayer.getDurationInSeconds(
                                    File(
                                        entries[index].audioPath
                                    )
                                )
                            )
                        )
                    }
                )

            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            audioPlayer.playerState.collect { playerState ->
                _state.update { state ->
                    val idx = state.journalEntries.indexOfFirst { it.audioPath == audioPlayer.filePath }
                    if (idx == -1) return@update state

                    val updatedEntries = state.journalEntries.toMutableList()
                    updatedEntries[idx] = updatedEntries[idx].copy(
                        playerState = playerState
                    )

                    state.copy(journalEntries = updatedEntries)
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.BottomSheetOpened -> {
                _state.update { it.copy(isBottomSheetOpen = true) }
            }

            HomeAction.BottomSheetClosed -> {
                _state.update { it.copy(isBottomSheetOpen = false) }
            }

            HomeAction.StartRecording -> {
                _state.update { it.copy(isRecording = true) }
                audioRecorder.startRecording(application = application)
            }

            HomeAction.PauseRecording -> {
                _state.update { it.copy(isRecording = false) }
                audioRecorder.pauseRecording()
            }

            HomeAction.ResumeRecording -> {
                _state.update { it.copy(isRecording = true) }
                audioRecorder.resumeRecording()
            }

            HomeAction.CancelRecording -> {
                _state.update { it.copy(isRecording = false) }
                audioRecorder.cancelRecording()
            }

            HomeAction.SaveRecording -> {
                _state.update { it.copy(isRecording = false) }
                audioRecorder.stopRecording()
                eventChannel.trySend(AudioRecorded("${application.cacheDir.absolutePath}/${audioRecorder.outputFile?.name}"))
            }

            is HomeAction.StartPlaying -> {
                audioPlayer.startPlayback(action.audioPath)
            }

            HomeAction.ResumePlaying -> {
                audioPlayer.resumePlayback()
            }

            HomeAction.PausePlaying -> {
                audioPlayer.pausePlayback()
            }

            HomeAction.StopPlaying -> {
                audioPlayer.stopPlayback()
            }


            HomeAction.SettingsClicked -> {
                // No implementation here
            }

        }
    }

    private fun formatCounter(counterInMillis: Long): String {
        val totalSeconds = counterInMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
} 