package com.uesar.journal.ui.model.mapper

import com.uesar.journal.domain.JournalEntry
import com.uesar.journal.domain.mood.Mood
import com.uesar.journal.domain.mood.moods
import com.uesar.journal.ui.model.JournalEntryUIState

fun JournalEntry.toUIState(): JournalEntryUIState {
    return JournalEntryUIState(
        id = id,
        title = title,
        audioPath = audioPath,
        mood = getMoodFromName(moodName),
        description = description,
        topics = topics,
        date = date
    )
}

private fun getMoodFromName(moodName: String) : Mood? {
    return moods.find { it.name == moodName }
}