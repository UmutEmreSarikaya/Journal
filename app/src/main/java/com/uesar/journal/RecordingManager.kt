package com.uesar.journal

import android.app.Application
import kotlinx.coroutines.flow.StateFlow

interface RecordingManager {
    val trackingTime: StateFlow<Long>
    fun startRecording(application: Application)
    fun stopRecording()
    fun pauseRecording()
    fun resumeRecording()
    fun cancelRecording()
}