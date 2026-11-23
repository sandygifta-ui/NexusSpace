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

    // Queue
    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue.asStateFlow()

    private val _queueIndex = MutableStateFlow(0)
    val queueIndex: StateFlow<Int> = _queueIndex.asStateFlow()

    // Current track
    private val _currentTrackId = MutableStateFlow<Long?>(null)
    val currentTrackId: StateFlow<Long?> = _currentTrackId.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    // Playback state
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Progress
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private val _totalSeconds = MutableStateFlow(0)
    val totalSeconds: StateFlow<Int> = _totalSeconds.asStateFlow()

    // Now Playing screen visibility
    private val _showNowPlaying = MutableStateFlow(false)
    val showNowPlaying: StateFlow<Boolean> = _showNowPlaying.asStateFlow()

    fun setQueue(tracks: List<Track>, startIndex: Int = 0) {
        _queue.value = tracks
        _queueIndex.value = startIndex
        val track = tracks.getOrNull(startIndex) ?: return
        playTrack(track)
    }

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
        // Set single track queue
        if (track != null && (_queue.value.isEmpty() || !_queue.value.any { it.id == trackId })) {
            _queue.value = listOf(track)
            _queueIndex.value = 0
        }
        playTrack(track ?: Track(id = trackId, preview = previewUrl))
    }

    fun skipNext() {
        val queue = _queue.value
        val nextIndex = _queueIndex.value + 1
        if (nextIndex < queue.size) {
            _queueIndex.value = nextIndex
            playTrack(queue[nextIndex])
        }
    }

    fun skipPrevious() {
        val queue = _queue.value
        // Jika sudah lebih dari 3 detik, restart lagu
        if (_elapsedSeconds.value > 3) {
            mediaPlayer?.seekTo(0)
            _progress.value = 0f
            _elapsedSeconds.value = 0
            return
        }
        val prevIndex = _queueIndex.value - 1
        if (prevIndex >= 0) {
            _queueIndex.value = prevIndex
            playTrack(queue[prevIndex])
        }
    }

    fun hasPrevious(): Boolean = _queueIndex.value > 0 || _elapsedSeconds.value > 3
    fun hasNext(): Boolean = _queueIndex.value < _queue.value.size - 1

    fun openNowPlaying() { _showNowPlaying.value = true }
    fun closeNowPlaying() { _showNowPlaying.value = false }

    private fun playTrack(track: Track) {
        stopInternal()
        _currentTrackId.value = track.id
        _currentTrack.value = track
        _isLoading.value = true
        _progress.value = 0f
        _elapsedSeconds.value = 0
        _totalSeconds.value = track.duration

        if (track.preview.isBlank()) {
            _isLoading.value = false
            return
        }

        mediaPlayer = MediaPlayer().apply {
            setDataSource(track.preview)
            setOnPreparedListener {
                _isLoading.value = false
                _isPlaying.value = true
                _totalSeconds.value = duration / 1000
                start()
                startProgressTracking()
            }
            setOnCompletionListener {
                _isPlaying.value = false
                stopProgressTracking()
                // Auto skip next
                val next = _queueIndex.value + 1
                if (next < _queue.value.size) {
                    _queueIndex.value = next
                    playTrack(_queue.value[next])
                } else {
                    _currentTrackId.value = null
                    _currentTrack.value = null
                    _progress.value = 0f
                }
            }
            setOnErrorListener { _, _, _ ->
                _isLoading.value = false
                _isPlaying.value = false
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
                        _totalSeconds.value = duration / 1000
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

    private fun stopInternal() {
        stopProgressTracking()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _isLoading.value = false
        _progress.value = 0f
        _elapsedSeconds.value = 0
    }

    fun stop() {
        stopInternal()
        _currentTrackId.value = null
        _currentTrack.value = null
        _showNowPlaying.value = false
    }

    fun release() { stop() }
}
