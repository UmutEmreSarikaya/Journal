package com.uesar.journal.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uesar.journal.Counter
import com.uesar.journal.RecordingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.Locale

class HomeViewModel(
    private val recordingManager: RecordingManager,
    private val application: Application,
    private val counter: Counter
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        counter.counter.onEach { counter ->
            _state.update { it.copy(recordingTime = formatCounter(counter)) }
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
                recordingManager.startRecording(application = application)
                counter.startOrResumeCounter()
            }

            is HomeAction.PauseRecording -> {
                _state.update { it.copy(isRecording = false) }
                recordingManager.pauseRecording()
                counter.pauseCounter()
            }

            is HomeAction.ResumeRecording -> {
                _state.update { it.copy(isRecording = true) }
                recordingManager.resumeRecording()
                counter.startOrResumeCounter()
            }

            is HomeAction.CancelRecording -> {
                _state.update { it.copy(isRecording = false) }
                recordingManager.cancelRecording()
                counter.resetCounter()
            }

            is HomeAction.SaveRecording -> {
                _state.update { it.copy(isRecording = false) }
                recordingManager.stopRecording()
                counter.resetCounter()
            }

            else -> Unit
        }
    }

    private fun formatCounter(counter: Int): String {
        val hours = counter / 3600
        val minutes = (counter % 3600) / 60
        val seconds = counter % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
} 