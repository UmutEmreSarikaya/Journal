package com.uesar.journal.data.local.mapper

import androidx.room.TypeConverter
import com.uesar.journal.domain.mood.Mood
import kotlinx.serialization.json.Json
import java.util.Date

class AppTypeConverter {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun fromMood(mood: Mood): String {
        return Json.encodeToString(mood)
    }

    @TypeConverter
    fun toMood(data: String): Mood {
        return Json.decodeFromString(data)
    }
}
