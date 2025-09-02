package com.uesar.journal.ui.newentry

import com.uesar.journal.domain.mood.Mood
import com.uesar.journal.ui.model.JournalEntryUIState

data class NewEntryState(
    val journalEntryUIState: JournalEntryUIState = JournalEntryUIState(),
    val topicText: String = "",
    val isTopicDropDownOpen: Boolean = false,
    val isBottomSheetOpen: Boolean = false,
    val currentSelectedMoodInBottomSheet: Mood? = null,
    val isNavigationDialogOpen: Boolean = false
)
