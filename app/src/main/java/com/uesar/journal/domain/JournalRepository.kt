package com.uesar.journal.domain

import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    suspend fun insertJournalEntry(journalEntry: JournalEntry)
    fun getJournalEntries() : Flow<List<JournalEntry>>
}