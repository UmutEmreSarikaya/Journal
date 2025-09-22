package com.uesar.journal.ui.home

import com.uesar.journal.ui.model.JournalEntryUIState

data class HomeState(
    val journalEntries: List<JournalEntryUIState> = emptyList(),
    val isBottomSheetOpen: Boolean = false,
    val isRecording: Boolean = false,
    val recordingTime: String = "00:00:00",
    val isFilterPopupOpen: Boolean = false,
    val selectedFilters: List<String> = emptyList()
)