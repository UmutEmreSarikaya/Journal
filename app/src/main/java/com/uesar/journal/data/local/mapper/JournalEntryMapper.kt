package com.uesar.journal.data.local.mapper

import com.uesar.journal.data.local.JournalEntryDTO
import com.uesar.journal.domain.JournalEntry

fun JournalEntry.toDto(): JournalEntryDTO {
    return JournalEntryDTO(
        id = id,
        title = title,
        recording = recording,
        mood = mood,
        description = description,
        topics = topics,
        date = date
    )
}

fun JournalEntryDTO.toDomain(): JournalEntry {
    return JournalEntry(
        id = id,
        title = title,
        recording = recording,
        mood = mood,
        description = description,
        topics = topics,
        date = date
    )
}