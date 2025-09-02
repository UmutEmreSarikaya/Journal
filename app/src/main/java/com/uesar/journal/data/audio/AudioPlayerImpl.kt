package com.uesar.journal.data.audio

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.uesar.journal.domain.audio.AudioPlayer
import com.uesar.journal.ui.PlayerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.io.File

class AudioPlayerImpl(private val context: Context) : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Idle)
    override val playerState: StateFlow<PlayerState> = _playerState
    override var filePath: String = ""

    override fun startPlayback(filePath: String) {
        stopPlayback()
        this.filePath = filePath
        mediaPlayer = MediaPlayer.create(context, filePath.toUri())
        mediaPlayer?.apply {
            start()
            _playerState.value = PlayerState.Playing
            setOnCompletionListener {
                _playerState.value = PlayerState.Completed
            }
        }
    }

    override fun pausePlayback() {
        mediaPlayer?.pause()
        _playerState.value = PlayerState.Paused
    }

    override fun resumePlayback() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing
    }

    override fun stopPlayback() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        _playerState.value = PlayerState.Idle
    }

    override fun getDuration(file: File): Int {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, file.toUri())
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration?.toInt() ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        } finally {
            retriever.release()
        }
    }

    override fun getCurrentPosition(): Flow<Int> = flow {
        while (mediaPlayer != null) {
            emit((mediaPlayer?.currentPosition ?: 0))
            delay(10)
        }
    }
}