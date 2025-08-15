package com.uesar.journal.ui.newentry

import com.uesar.journal.Mood
import com.uesar.journal.domain.Playback

data class NewEntryState(
    val title: String = "",
    val playback: Playback = Playback(false, "00:00", "00:00", ""),
    val topicText: String = "",
    val description: String = "",
    val topics: List<String> = emptyList(),
    val isTopicDropDownOpen: Boolean = false,
    val isBottomSheetOpen: Boolean = false,
    val currentSelectedMoodInBottomSheet: Mood? = null,
    val selectedMood: Mood? = null,
    val isNavigationDialogOpen: Boolean = false
)
