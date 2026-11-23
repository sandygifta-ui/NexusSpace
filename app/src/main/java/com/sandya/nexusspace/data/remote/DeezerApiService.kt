package com.sandya.nexusspace.data.remote

import com.sandya.nexusspace.data.model.Album
import com.sandya.nexusspace.data.model.AlbumListResponse
import com.sandya.nexusspace.data.model.Artist
import com.sandya.nexusspace.data.model.ArtistListResponse
import com.sandya.nexusspace.data.model.ChartResponse
import com.sandya.nexusspace.data.model.GenreListResponse
import com.sandya.nexusspace.data.model.PlaylistListResponse
import com.sandya.nexusspace.data.model.SearchResponse
import com.sandya.nexusspace.data.model.TrackListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiService {

    // Chart (trending)
    @GET("chart")
    suspend fun getChart(): ChartResponse

    // Search tracks
    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse

    // Genre list
    @GET("genre")
    suspend fun getGenres(): GenreListResponse

    // Top tracks by genre
    @GET("genre/{id}/artists")
    suspend fun getArtistsByGenre(@Path("id") genreId: Long): ArtistListResponse

    // Artist detail
    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Long): Artist

    // Artist top tracks
    @GET("artist/{id}/top?limit=20")
    suspend fun getArtistTopTracks(@Path("id") artistId: Long): TrackListResponse

    // Artist albums
    @GET("artist/{id}/albums")
    suspend fun getArtistAlbums(@Path("id") artistId: Long): AlbumListResponse

    // Album detail
    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Long): Album

    // Editorial playlists (curated)
    @GET("editorial/0/charts")
    suspend fun getEditorialCharts(): ChartResponse
}
