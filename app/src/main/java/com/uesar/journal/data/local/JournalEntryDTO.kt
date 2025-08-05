package com.uesar.journal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uesar.journal.Mood
import com.uesar.journal.Recording
import java.util.Date

@Entity
data class JournalEntryDTO(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val recording: Recording,
    val mood: Mood,
    val description: String,
    val topics: List<String>,
    val date: Date
)
