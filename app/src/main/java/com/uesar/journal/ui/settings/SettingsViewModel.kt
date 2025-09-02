package com.uesar.journal.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uesar.journal.domain.settings.SettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsPreferences: SettingsPreferences): ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        settingsPreferences.observeDefaultMood().onEach { mood ->
            _state.update { it.copy(selectedMood = mood) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.NavigateBack -> {
                // No implementation here
            }

            is SettingsAction.OnMoodClicked -> {
                viewModelScope.launch {
                    settingsPreferences.saveDefaultMood(action.mood)
                }
            }
        }
    }
}