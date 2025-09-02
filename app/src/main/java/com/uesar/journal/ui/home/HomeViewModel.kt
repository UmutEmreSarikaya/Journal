package com.uesar.journal.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uesar.journal.domain.player.AudioPlayer
import com.uesar.journal.AudioRecorder
import com.uesar.journal.domain.JournalRepository
import com.uesar.journal.ui.home.HomeEvent.*
import com.uesar.journal.ui.model.mapper.toUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private var positionJob: Job? = null


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
                            totalTime = audioPlayer.getDuration(
                                File(
                                    entries[index].audioPath
                                )
                            )
                        )
                    }
                )
            }
        }.launchIn(viewModelScope)

        audioPlayer.playerState.onEach { playerState ->
            _state.update { state ->
                val idx =
                    state.journalEntries.indexOfFirst { it.audioPath == audioPlayer.filePath }
                if (idx == -1) return@update state

                val updatedEntries = state.journalEntries.toMutableList()
                updatedEntries[idx] = updatedEntries[idx].copy(
                    playerState = playerState
                )

                state.copy(journalEntries = updatedEntries)
            }
        }.launchIn(viewModelScope)
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
                positionJob?.cancel()

                audioPlayer.startPlayback(action.audioPath)
                positionJob = viewModelScope.launch {
                    audioPlayer.getCurrentPosition().collect { currentTime ->
                        val index =
                            state.value.journalEntries.indexOfFirst { it.audioPath == action.audioPath }

                        val updatedEntries = state.value.journalEntries.toMutableList()
                        updatedEntries[index] = updatedEntries[index].copy(
                            currentTime = currentTime
                        )
                        _state.update { state ->
                            state.copy(journalEntries = updatedEntries)
                        }
                    }
                }
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
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
} 