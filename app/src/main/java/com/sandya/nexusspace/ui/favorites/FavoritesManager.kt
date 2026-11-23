package com.sandya.nexusspace.ui.favorites

import com.sandya.nexusspace.data.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FavoritesManager {

    private val _favorites = MutableStateFlow<List<Track>>(emptyList())
    val favorites: StateFlow<List<Track>> = _favorites.asStateFlow()

    fun toggleFavorite(track: Track) {
        val current = _favorites.value.toMutableList()
        if (current.any { it.id == track.id }) {
            current.removeAll { it.id == track.id }
        } else {
            current.add(0, track)
        }
        _favorites.value = current
    }

    fun isFavorite(trackId: Long): Boolean {
        return _favorites.value.any { it.id == trackId }
    }
}
