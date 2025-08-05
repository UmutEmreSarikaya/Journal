package com.uesar.journal

interface AudioPlayer {
    fun startPlayback(filePath: String)
    fun pausePlayback()
    fun resumePlayback()
    fun stopPlayback()
}