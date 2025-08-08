package com.uesar.journal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AudioPlayer {
    val isPlaying: StateFlow<Boolean>
    fun startPlayback(filePath: String)
    fun pausePlayback()
    fun resumePlayback()
    fun stopPlayback()
    fun getDuration(file: File): Int
    fun getCurrentPosition(): Flow<Int>
}