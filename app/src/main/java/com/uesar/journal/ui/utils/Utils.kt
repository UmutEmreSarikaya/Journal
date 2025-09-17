package com.uesar.journal.ui.utils

import com.uesar.journal.ui.model.JournalEntryUIState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    fun formatSecondsToMinutes(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }

    fun extractHourAsTextFromDate(date: Date): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = formatter.format(date)
        return timeString
    }

    fun formatRecordedDate(recordedTimeInMillis: Long): String {
        val now = Calendar.getInstance()
        val recorded = Calendar.getInstance().apply { this.timeInMillis = recordedTimeInMillis }

        val dateFormatSameYear = SimpleDateFormat("d MMM", Locale.getDefault())
        val dateFormatOtherYear = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

        if (now.get(Calendar.YEAR) == recorded.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == recorded.get(Calendar.DAY_OF_YEAR)
        ) {
            return "Today"
        }

        now.add(Calendar.DAY_OF_YEAR, -1)
        if (now.get(Calendar.YEAR) == recorded.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == recorded.get(Calendar.DAY_OF_YEAR)
        ) {
            return "Yesterday"
        }

        now.timeInMillis = System.currentTimeMillis()

        // If same year, show "day month"
        return if (now.get(Calendar.YEAR) == recorded.get(Calendar.YEAR)) {
            dateFormatSameYear.format(recorded.time)
        } else {
            dateFormatOtherYear.format(recorded.time)
        }
    }

    fun groupEntriesByDate(entries: List<JournalEntryUIState>): Map<String, List<JournalEntryUIState>> {
        return entries
            .sortedByDescending { it.date } // newest first
            .groupBy { formatRecordedDate(it.date.time) }
    }
}