package com.uesar.journal.domain

import java.util.Date

data class JournalEntry(
    val id: Int = 0,
    val title: String,
    val audioPath: String,
    val moodName: String,
    val description: String,
    val topics: List<String>,
    val date: Date
)
