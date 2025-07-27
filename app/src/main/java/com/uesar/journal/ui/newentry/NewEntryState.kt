package com.uesar.journal.ui.newentry

import com.uesar.journal.Mood

data class NewEntryState(
    val title: String = "",
    val description: String = "",
    val topics: List<String> = emptyList(),
    val isBottomSheetOpen: Boolean = false,
    val currentSelectedMoodInBottomSheet: Mood? = null,
    val selectedMood: Mood? = null
)
