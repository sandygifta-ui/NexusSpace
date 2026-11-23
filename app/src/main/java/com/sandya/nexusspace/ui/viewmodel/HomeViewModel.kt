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

data class HomeUiState(
    val isLoading: Boolean = true,
    val trendingTracks: List<Track> = emptyList(),
    val trendingAlbums: List<Album> = emptyList(),
    val trendingArtists: List<Artist> = emptyList(),
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val repository = DeezerRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadChart()
    }

    fun loadChart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getChart()
                .onSuccess { chart ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        trendingTracks = chart.tracks.data.take(10),
                        trendingAlbums = chart.albums.data.take(10),
                        trendingArtists = chart.artists.data.take(10)
                    )
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        error = error.message ?: "Unknown error"
                    )
                }
        }
    }
}
