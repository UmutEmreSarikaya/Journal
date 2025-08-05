package com.uesar.journal

import android.media.MediaPlayer
import java.io.IOException

class AudioPlayerImpl : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var isPaused: Boolean = false

    override fun startPlayback(filePath: String) {
        stopPlayback()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            setOnCompletionListener {
                stopPlayback()
            }
        }

        isPaused = false
    }

    override fun pausePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPaused = true
            }
        }
    }

    override fun resumePlayback() {
        mediaPlayer?.let {
            if (isPaused) {
                it.start()
                isPaused = false
            }
        }
    }

    override fun stopPlayback() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        isPaused = false
    }
}
