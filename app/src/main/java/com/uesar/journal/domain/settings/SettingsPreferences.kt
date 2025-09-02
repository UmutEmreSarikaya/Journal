package com.uesar.journal.domain.settings

import com.uesar.journal.domain.mood.Mood
import kotlinx.coroutines.flow.Flow

interface SettingsPreferences {
    suspend fun saveDefaultTopics(topics: List<String>)
    fun observeDefaultTopics(): Flow<List<String>>

    suspend fun saveDefaultMood(mood: Mood)
    fun observeDefaultMood(): Flow<Mood>
}