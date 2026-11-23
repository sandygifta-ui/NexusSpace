package com.sandya.nexusspace.ui.player

import android.media.MediaPlayer
import com.sandya.nexusspace.data.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressJob: Job? = null

    private val _currentTrackId = MutableStateFlow<Long?>(null)
    val currentTrackId: StateFlow<Long?> = _currentTrackId.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Progress 0f..1f
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    // Elapsed seconds
    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    fun playOrPause(trackId: Long, previewUrl: String, track: Track? = null) {
        if (_currentTrackId.value == trackId) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                _isPlaying.value = false
                stopProgressTracking()
            } else {
                mediaPlayer?.start()
                _isPlaying.value = true
                startProgressTracking()
            }
            return
        }

        stop()
        _currentTrackId.value = trackId
        _currentTrack.value = track
        _isLoading.value = true
        _progress.value = 0f
        _elapsedSeconds.value = 0

        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            setOnPreparedListener {
                _isLoading.value = false
                _isPlaying.value = true
                start()
                startProgressTracking()
            }
            setOnCompletionListener {
                _isPlaying.value = false
                _progress.value = 1f
                stopProgressTracking()
                _currentTrackId.value = null
                _currentTrack.value = null
                _progress.value = 0f
                _elapsedSeconds.value = 0
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

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (true) {
                val mp = mediaPlayer ?: break
                if (mp.isPlaying) {
                    val current = mp.currentPosition
                    val duration = mp.duration
                    if (duration > 0) {
                        _progress.value = current.toFloat() / duration.toFloat()
                        _elapsedSeconds.value = current / 1000
                    }
                }
                delay(500)
            }
        }
    }

    private fun stopProgressTracking() {
        progressJob?.cancel()
        progressJob = null
    }

    fun stop() {
        stopProgressTracking()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _isLoading.value = false
        _currentTrackId.value = null
        _currentTrack.value = null
        _progress.value = 0f
        _elapsedSeconds.value = 0
    }

    fun release() {
        stop()
    }
}
