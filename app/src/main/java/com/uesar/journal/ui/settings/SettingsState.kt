package com.uesar.journal.ui.settings

import com.uesar.journal.domain.mood.Mood

data class SettingsState(
    val selectedMood: Mood? = null,
    val isTopicDropDownOpen: Boolean = false
)
