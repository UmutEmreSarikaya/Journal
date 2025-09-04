package com.uesar.journal.ui.model

import com.uesar.journal.domain.mood.Mood
import com.uesar.journal.domain.PlayerState
import java.util.Date

data class JournalEntryUIState (
    val id: Int = 0,
    val title: String = "",
    val mood: Mood? = null,
    val description: String = "",
    val topics: List<String> = emptyList(),
    val date: Date = Date(),
    val audioPath: String = "",
    val playerState: PlayerState = PlayerState.Idle,
    val totalTime: Int = -1,
    val currentTime: Int = -1
)