package com.uesar.journal

import android.app.Application
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class RecordingManagerImpl : RecordingManager{
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null

    override fun startRecording(application: Application) {
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
    }

    override fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    override fun pauseRecording() {
        mediaRecorder?.pause()
    }

    override fun resumeRecording() {
        mediaRecorder?.resume()
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
    }
}