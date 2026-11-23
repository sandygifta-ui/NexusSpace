package com.sandya.nexusspace.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandya.nexusspace.data.model.Artist
import com.sandya.nexusspace.data.model.Genre
import com.sandya.nexusspace.data.repository.DeezerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GenreUiState(
    val isLoading: Boolean = true,
    val genres: List<Genre> = emptyList(),
    val error: String? = null
)

data class GenreDetailUiState(
    val isLoading: Boolean = false,
    val genre: Genre? = null,
    val artists: List<Artist> = emptyList(),
    val error: String? = null
)

class GenreViewModel : ViewModel() {

    private val repository = DeezerRepository()

    private val _genreState = MutableStateFlow(GenreUiState())
    val genreState: StateFlow<GenreUiState> = _genreState.asStateFlow()

    private val _detailState = MutableStateFlow(GenreDetailUiState())
    val detailState: StateFlow<GenreDetailUiState> = _detailState.asStateFlow()

    init {
        loadGenres()
    }

    fun loadGenres() {
        viewModelScope.launch {
            _genreState.value = GenreUiState(isLoading = true)
            repository.getGenres()
                .onSuccess { response ->
                    _genreState.value = GenreUiState(
                        isLoading = false,
                        genres = response.data.filter { genre ->
                            genre.id != 0L && genre.name !in listOf(
                                "Kids", "Audiobooks", "Podcasts",
                                "Radio", "Talk", "Comedy", "Spoken Word"
                            )
                        }
                    )
                }
                .onFailure { error ->
                    _genreState.value = GenreUiState(
                        isLoading = false,
                        error = error.message ?: "Failed to load genres"
                    )
                }
        }
    }

    fun loadGenreArtists(genre: Genre) {
        viewModelScope.launch {
            _detailState.value = GenreDetailUiState(isLoading = true, genre = genre)
            repository.getGenres().onSuccess { }
            // Load artists by genre
            val result = runCatching {
                com.sandya.nexusspace.data.remote.RetrofitInstance.api
                    .getArtistsByGenre(genre.id)
            }
            result
                .onSuccess { response ->
                    _detailState.value = GenreDetailUiState(
                        isLoading = false,
                        genre = genre,
                        artists = response.data
                            .filter { it.picture.isNotBlank() || it.pictureMedium.isNotBlank() }
                            .sortedByDescending { it.nbFan }
                            .take(20)
                    )
                }
                .onFailure { error ->
                    _detailState.value = GenreDetailUiState(
                        isLoading = false,
                        genre = genre,
                        error = error.message ?: "Failed to load artists"
                    )
                }
        }
    }
}
