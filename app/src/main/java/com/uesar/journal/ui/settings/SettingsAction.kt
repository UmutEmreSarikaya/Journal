package com.uesar.journal.ui.settings

sealed interface SettingsAction {
    data object NavigateBack : SettingsAction
}