package com.uesar.journal.data.recorder

import android.app.Application
import android.media.MediaRecorder
import android.os.Build
import com.uesar.journal.domain.recorder.AudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class AudioRecorderImpl(private val application: Application) : AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    override var outputFile: File? = null
    private var durationJob: Job? = null
    private var isRecording = false
    private var isPaused = false
    private val _recordingTime = MutableStateFlow<Long>(0)
    override val recordingTime = _recordingTime.asStateFlow()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun startRecording() {
        outputFile = File(application.cacheDir, "audio_${System.currentTimeMillis()}.mp3")

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(application)
        } else {
            MediaRecorder()
        }

        mediaRecorder = mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            prepare()
            start()
        }
        isPaused = false
        isRecording = true
        startTrackingDuration()
    }

    override fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording = false
        _recordingTime.value = 0
    }

    override fun pauseRecording() {
        isPaused = true
        isRecording = false
        mediaRecorder?.pause()
    }

    override fun resumeRecording() {
        isPaused = false
        isRecording = true
        mediaRecorder?.resume()
        startTrackingDuration()
    }

    override fun cancelRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
            mediaRecorder = null
            outputFile?.let {
                if (it.exists()) {
                    it.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaRecorder = null
            outputFile = null
        }
        isRecording = false
        _recordingTime.value = 0
    }

    private fun startTrackingDuration() {
        durationJob = coroutineScope.launch {
            var lastTime = System.currentTimeMillis()
            while (isRecording && !isPaused) {
                delay(10L)
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastTime
                _recordingTime.value += elapsedTime
                lastTime = System.currentTimeMillis()
            }
        }
    }
}