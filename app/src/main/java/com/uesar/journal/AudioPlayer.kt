package com.uesar.journal

import com.uesar.journal.ui.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AudioPlayer {
    val filePath: String
    val playerState: StateFlow<PlayerState>
    fun startPlayback(filePath: String)
    fun pausePlayback()
    fun resumePlayback()
    fun stopPlayback()
    fun getDuration(file: File): Int
    fun getCurrentPosition(): Flow<Int>
}