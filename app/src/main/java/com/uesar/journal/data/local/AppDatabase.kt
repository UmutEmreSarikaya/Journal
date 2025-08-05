package com.uesar.journal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uesar.journal.data.local.mapper.AppTypeConverter

@Database(entities = [JournalEntryDTO::class], version = 1)
@TypeConverters(AppTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): JournalEntryDao
}