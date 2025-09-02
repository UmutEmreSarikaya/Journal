package com.uesar.journal.ui.settings

import com.uesar.journal.Mood

sealed interface SettingsAction {
    data object NavigateBack : SettingsAction
    data class OnMoodClicked(val mood: Mood) : SettingsAction
}