package com.uesar.journal.domain.recorder

import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AudioRecorder {
    val outputFile: File?
    val recordingTime: StateFlow<Long>
    fun startRecording()
    fun stopRecording()
    fun pauseRecording()
    fun resumeRecording()
    fun cancelRecording()
}