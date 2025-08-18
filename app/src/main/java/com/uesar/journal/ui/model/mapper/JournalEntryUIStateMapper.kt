package com.uesar.journal.ui.model.mapper

import com.uesar.journal.domain.JournalEntry
import com.uesar.journal.ui.model.JournalEntryUIState

fun JournalEntry.toUIState(): JournalEntryUIState {
    return JournalEntryUIState(
        id = id,
        title = title,
        audioPath = audioPath,
        mood = mood,
        description = description,
        topics = topics,
        date = date
    )
}