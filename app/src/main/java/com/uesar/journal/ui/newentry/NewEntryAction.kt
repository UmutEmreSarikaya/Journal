package com.uesar.journal.ui.newentry

sealed interface NewEntryAction {
    data class OnTitleChanged(val title: String) : NewEntryAction
    data object NavigateBack : NewEntryAction
}