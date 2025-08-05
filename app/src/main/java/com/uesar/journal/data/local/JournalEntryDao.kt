package com.uesar.journal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalEntryDao {
    @Insert
    suspend fun insertEntry(entry: JournalEntryDTO)

    @Query("SELECT * FROM JournalEntryDTO")
    fun getAllEntries(): Flow<List<JournalEntryDTO>>
}