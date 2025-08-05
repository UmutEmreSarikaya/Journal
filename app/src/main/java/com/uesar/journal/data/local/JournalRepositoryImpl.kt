package com.uesar.journal.data.local

import com.uesar.journal.data.local.mapper.toDomain
import com.uesar.journal.data.local.mapper.toDto
import com.uesar.journal.domain.JournalEntry
import com.uesar.journal.domain.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JournalRepositoryImpl(private val dao: JournalEntryDao): JournalRepository {
    override suspend fun insertJournalEntry(journalEntry: JournalEntry) {
        dao.insertEntry(journalEntry.toDto())
    }

    override fun getJournalEntries(): Flow<List<JournalEntry>> {
        return dao.getAllEntries().map { entries -> entries.map { it.toDomain() } }
    }
}