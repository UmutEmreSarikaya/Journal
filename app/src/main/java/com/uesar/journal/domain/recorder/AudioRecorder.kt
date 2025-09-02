package com.uesar.journal.domain.recorder

import android.app.Application
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AudioRecorder {
    val outputFile: File?
    val recordingTime: StateFlow<Long>
    fun startRecording(application: Application)
    fun stopRecording()
    fun pauseRecording()
    fun resumeRecording()
    fun cancelRecording()
}