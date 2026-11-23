package com.sandya.nexusspace.ui.player

import android.media.MediaPlayer
import com.sandya.nexusspace.data.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null

    private val _currentTrackId = MutableStateFlow<Long?>(null)
    val currentTrackId: StateFlow<Long?> = _currentTrackId.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun playOrPause(trackId: Long, previewUrl: String, track: Track? = null) {
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

        stop()
        _currentTrackId.value = trackId
        _currentTrack.value = track
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
                _currentTrack.value = null
            }
            setOnErrorListener { _, _, _ ->
                _isLoading.value = false
                _isPlaying.value = false
                _currentTrackId.value = null
                _currentTrack.value = null
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
        _currentTrack.value = null
    }

    fun release() {
        stop()
    }
}
