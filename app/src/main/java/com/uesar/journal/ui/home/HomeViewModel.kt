package com.uesar.journal.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun onIntent(intent: HomeAction) {
        when (intent) {
            is HomeAction.AddEntryClicked -> {

            }
            is HomeAction.SettingsClicked -> {

            }
        }
    }
} 