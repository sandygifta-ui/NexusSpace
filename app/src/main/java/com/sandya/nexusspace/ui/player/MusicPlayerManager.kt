package com.sandya.nexusspace.ui.player

import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null

    private val _currentTrackId = MutableStateFlow<Long?>(null)
    val currentTrackId: StateFlow<Long?> = _currentTrackId.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun playOrPause(trackId: Long, previewUrl: String) {
        // Kalau lagu yang sama diklik, toggle play/pause
        if (_currentTrackId.value == trackId) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                _isPlaying.value = false
            } else {
                mediaPlayer?.start()
                _isPlaying.value = true
            }
            return
        }

        // Lagu berbeda — stop yang lama, play yang baru
        stop()
        _currentTrackId.value = trackId
        _isLoading.value = true

        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            setOnPreparedListener {
                _isLoading.value = false
                _isPlaying.value = true
                start()
            }
            setOnCompletionListener {
                _isPlaying.value = false
                _currentTrackId.value = null
            }
            setOnErrorListener { _, _, _ ->
                _isLoading.value = false
                _isPlaying.value = false
                _currentTrackId.value = null
                true
            }
            prepareAsync()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _isLoading.value = false
        _currentTrackId.value = null
    }

    fun release() {
        stop()
    }
}
