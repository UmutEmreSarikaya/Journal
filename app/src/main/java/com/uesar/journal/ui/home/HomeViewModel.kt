package com.uesar.journal.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

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
            }
            is HomeAction.PauseRecording -> {
                _state.update { it.copy(isRecording = false) }
            }
            is HomeAction.ResumeRecording -> {
                _state.update { it.copy(isRecording = true) }
            }
            is HomeAction.CancelRecording -> {
                _state.update { it.copy(isRecording = false) }
            }
            else -> Unit
        }
    }
} 