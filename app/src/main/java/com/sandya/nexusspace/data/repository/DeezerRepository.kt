package com.sandya.nexusspace.data.repository

import com.sandya.nexusspace.data.model.Album
import com.sandya.nexusspace.data.model.Artist
import com.sandya.nexusspace.data.model.ChartResponse
import com.sandya.nexusspace.data.model.GenreListResponse
import com.sandya.nexusspace.data.model.SearchResponse
import com.sandya.nexusspace.data.model.TrackListResponse
import com.sandya.nexusspace.data.remote.RetrofitInstance

class DeezerRepository {

    private val api = RetrofitInstance.api

    suspend fun getChart(): Result<ChartResponse> = runCatching {
        api.getChart()
    }

    suspend fun searchTracks(query: String): Result<SearchResponse> = runCatching {
        api.searchTracks(query)
    }

    suspend fun getGenres(): Result<GenreListResponse> = runCatching {
        api.getGenres()
    }

    suspend fun getArtist(artistId: Long): Result<Artist> = runCatching {
        api.getArtist(artistId)
    }

    suspend fun getArtistTopTracks(artistId: Long): Result<TrackListResponse> = runCatching {
        api.getArtistTopTracks(artistId)
    }

    suspend fun getAlbum(albumId: Long): Result<Album> = runCatching {
        api.getAlbum(albumId)
    }
}
