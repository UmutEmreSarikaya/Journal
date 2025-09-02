package com.uesar.journal.domain

import com.uesar.journal.domain.mood.Mood
import java.util.Date

data class JournalEntry(
    val id: Int = 0,
    val title: String,
    val audioPath: String,
    val mood: Mood,
    val description: String,
    val topics: List<String>,
    val date: Date
)
