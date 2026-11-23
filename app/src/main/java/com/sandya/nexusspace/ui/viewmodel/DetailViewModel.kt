package com.sandya.nexusspace.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandya.nexusspace.data.model.Album
import com.sandya.nexusspace.data.model.Artist
import com.sandya.nexusspace.data.model.Track
import com.sandya.nexusspace.data.repository.DeezerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ArtistDetailUiState(
    val isLoading: Boolean = true,
    val artist: Artist? = null,
    val topTracks: List<Track> = emptyList(),
    val error: String? = null
)

data class AlbumDetailUiState(
    val isLoading: Boolean = true,
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val error: String? = null
)

class DetailViewModel : ViewModel() {

    private val repository = DeezerRepository()

    private val _artistState = MutableStateFlow(ArtistDetailUiState())
    val artistState: StateFlow<ArtistDetailUiState> = _artistState.asStateFlow()

    private val _albumState = MutableStateFlow(AlbumDetailUiState())
    val albumState: StateFlow<AlbumDetailUiState> = _albumState.asStateFlow()

    fun loadArtist(artistId: Long) {
        viewModelScope.launch {
            _artistState.value = ArtistDetailUiState(isLoading = true)
            val artistResult = repository.getArtist(artistId)
            val tracksResult = repository.getArtistTopTracks(artistId)

            if (artistResult.isSuccess) {
                _artistState.value = ArtistDetailUiState(
                    isLoading = false,
                    artist = artistResult.getOrNull(),
                    topTracks = tracksResult.getOrNull()?.data ?: emptyList()
                )
            } else {
                _artistState.value = ArtistDetailUiState(
                    isLoading = false,
                    error = artistResult.exceptionOrNull()?.message ?: "Failed to load artist"
                )
            }
        }
    }

    fun loadAlbum(albumId: Long) {
        viewModelScope.launch {
            _albumState.value = AlbumDetailUiState(isLoading = true)
            repository.getAlbum(albumId)
                .onSuccess { album ->
                    _albumState.value = AlbumDetailUiState(
                        isLoading = false,
                        album = album,
                        tracks = album.tracks?.data ?: emptyList()
                    )
                }
                .onFailure { error ->
                    _albumState.value = AlbumDetailUiState(
                        isLoading = false,
                        error = error.message ?: "Failed to load album"
                    )
                }
        }
    }
}
