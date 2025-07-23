package com.uesar.journal

import android.app.Application

interface RecordingManager {
    fun startRecording(application: Application)
    fun stopRecording()
    fun pauseRecording()
    fun resumeRecording()
    fun cancelRecording()
}