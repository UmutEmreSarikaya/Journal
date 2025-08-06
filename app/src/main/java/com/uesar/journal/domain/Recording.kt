package com.uesar.journal.domain

data class Recording(
    val isPlaying: Boolean,
    val totalTime: String,
    val currentTime: String,
    val audioPath: String
)