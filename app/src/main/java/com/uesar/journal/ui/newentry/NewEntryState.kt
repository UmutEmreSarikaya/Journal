package com.uesar.journal.ui.newentry

import com.uesar.journal.Mood

data class NewEntryState(
    val title: String = "",
    val topicText: String = "",
    val description: String = "",
    val topics: List<String> = listOf(
        "gasgrgweggwgwe",
        "topic2r32",
        "topic12432",
        "topicx",
        "yds",
        "asf",
    ),
    val isTopicDropDownOpen: Boolean = false,
    val isBottomSheetOpen: Boolean = false,
    val currentSelectedMoodInBottomSheet: Mood? = null,
    val selectedMood: Mood? = null,
    val isNavigationDialogOpen: Boolean = false
)
