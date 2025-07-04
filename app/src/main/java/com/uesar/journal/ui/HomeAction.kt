package com.uesar.journal.ui

sealed interface HomeAction {
    data object AddEntryClicked : HomeAction
    data object SettingsClicked : HomeAction
}