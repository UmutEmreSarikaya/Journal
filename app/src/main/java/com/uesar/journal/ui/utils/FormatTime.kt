package com.uesar.journal.ui.utils

fun formatSecondsToMinutes(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}