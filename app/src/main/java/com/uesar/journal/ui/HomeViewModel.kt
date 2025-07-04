package com.uesar.journal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun onIntent(intent: HomeAction) {
        when (intent) {
            is HomeAction.AddEntryClicked -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(entryCount = _state.value.entryCount + 1)
                }
            }
            is HomeAction.SettingsClicked -> {

            }
        }
    }
} 