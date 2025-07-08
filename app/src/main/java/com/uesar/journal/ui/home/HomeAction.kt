package com.uesar.journal.ui.home

sealed interface HomeAction {
    data object AddEntryClicked : HomeAction
    data object SettingsClicked : HomeAction
}