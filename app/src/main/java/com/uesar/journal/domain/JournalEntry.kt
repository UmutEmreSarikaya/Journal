package com.uesar.journal.domain

import com.uesar.journal.Mood
import com.uesar.journal.Recording
import java.util.Date

data class JournalEntry(
    val id: Int = 0,
    val title: String,
    val recording: Recording,
    val mood: Mood,
    val description: String,
    val topics: List<String>,
    val date: Date
)
