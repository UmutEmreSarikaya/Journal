package com.uesar.journal

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.core.net.toUri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.io.File

class AudioPlayerImpl(private val context: Context) : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying

    override fun startPlayback(filePath: String) {
        mediaPlayer = MediaPlayer.create(context, filePath.toUri())
        mediaPlayer?.apply {
            start()
            _isPlaying.value = true
            setOnCompletionListener {
                _isPlaying.value = false
            }
        }
    }

    override fun pausePlayback() {
        mediaPlayer?.pause()
        _isPlaying.value = false
    }

    override fun resumePlayback() {
        mediaPlayer?.start()
        _isPlaying.value = true
    }

    override fun stopPlayback() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        _isPlaying.value = false
    }

    override fun getDurationInSeconds(file: File): Int {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, file.toUri())
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration?.toInt()?.div(1000) ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        } finally {
            retriever.release()
        }
    }

    override fun getCurrentPosition(): Flow<Int> = flow {
        while (mediaPlayer != null) {
            emit((mediaPlayer?.currentPosition ?: 0) / 1000)
            delay(500)
        }
    }

}
