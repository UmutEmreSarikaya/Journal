package com.uesar.journal.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uesar.journal.RecordingManager
import com.uesar.journal.AudioRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.Locale

class HomeViewModel(
    private val recordingManager: RecordingManager,
    private val audioRecorder: AudioRecorder,
    private val application: Application,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        audioRecorder.trackingTime.onEach { time ->
            _state.update { it.copy(recordingTime = formatCounter(time)) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.BottomSheetOpened -> {
                _state.update { it.copy(isBottomSheetOpen = true) }
            }

            is HomeAction.BottomSheetClosed -> {
                _state.update { it.copy(isBottomSheetOpen = false) }
            }

            is HomeAction.StartRecording -> {
                _state.update { it.copy(isRecording = true) }
                audioRecorder.startRecording(application = application)
            }

            is HomeAction.PauseRecording -> {
                _state.update { it.copy(isRecording = false) }
                audioRecorder.pauseRecording()
            }

            is HomeAction.ResumeRecording -> {
                _state.update { it.copy(isRecording = true) }
                audioRecorder.resumeRecording()
            }

            is HomeAction.CancelRecording -> {
                _state.update { it.copy(isRecording = false) }
                audioRecorder.cancelRecording()
            }

            is HomeAction.SaveRecording -> {
                _state.update { it.copy(isRecording = false) }
                audioRecorder.stopRecording()
            }

            else -> Unit
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