package com.uesar.journal.ui.home

import com.uesar.journal.domain.JournalEntry
import com.uesar.journal.domain.Recording

data class HomeState(
    val journalEntries: List<JournalEntry> = emptyList(),
    val recordings: List<Recording> = emptyList(),
    val isBottomSheetOpen: Boolean = false,
    val isRecording: Boolean = false,
    val recordingTime: String = "00:00:00"
) 