package com.uesar.journal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class JournalEntryDTO(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val audioPath: String,
    val moodName: String,
    val description: String,
    val topics: List<String>,
    val date: Date
)
