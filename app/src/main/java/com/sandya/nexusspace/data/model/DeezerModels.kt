package com.sandya.nexusspace.data.model

import com.google.gson.annotations.SerializedName

// ── Track ──────────────────────────────────────────────────────────────────

data class Track(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("preview") val preview: String = "",
    @SerializedName("rank") val rank: Long = 0,
    @SerializedName("artist") val artist: Artist = Artist(),
    @SerializedName("album") val album: Album = Album()
)

data class TrackListResponse(
    @SerializedName("data") val data: List<Track> = emptyList(),
    @SerializedName("total") val total: Int = 0
)

// ── Artist ─────────────────────────────────────────────────────────────────

data class Artist(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("picture") val picture: String = "",
    @SerializedName("picture_medium") val pictureMedium: String = "",
    @SerializedName("picture_xl") val pictureXl: String = "",
    @SerializedName("nb_fan") val nbFan: Long = 0,
    @SerializedName("nb_album") val nbAlbum: Int = 0
)

data class ArtistListResponse(
    @SerializedName("data") val data: List<Artist> = emptyList(),
    @SerializedName("total") val total: Int = 0
)

// ── Album ──────────────────────────────────────────────────────────────────

data class Album(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("cover") val cover: String = "",
    @SerializedName("cover_medium") val coverMedium: String = "",
    @SerializedName("cover_big") val coverBig: String = "",
    @SerializedName("cover_xl") val coverXl: String = "",
    @SerializedName("artist") val artist: Artist? = null,
    @SerializedName("tracks") val tracks: TrackListResponse? = null
)

data class AlbumListResponse(
    @SerializedName("data") val data: List<Album> = emptyList(),
    @SerializedName("total") val total: Int = 0
)

// ── Genre / Chart ──────────────────────────────────────────────────────────

data class Genre(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("picture") val picture: String = "",
    @SerializedName("picture_medium") val pictureMedium: String = ""
)

data class GenreListResponse(
    @SerializedName("data") val data: List<Genre> = emptyList()
)

data class ChartResponse(
    @SerializedName("tracks") val tracks: TrackListResponse = TrackListResponse(),
    @SerializedName("albums") val albums: AlbumListResponse = AlbumListResponse(),
    @SerializedName("artists") val artists: ArtistListResponse = ArtistListResponse()
)

// ── Search ─────────────────────────────────────────────────────────────────

data class SearchResponse(
    @SerializedName("data") val data: List<Track> = emptyList(),
    @SerializedName("total") val total: Int = 0
)

// ── Playlist ───────────────────────────────────────────────────────────────

data class Playlist(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("picture") val picture: String = "",
    @SerializedName("picture_medium") val pictureMedium: String = "",
    @SerializedName("nb_tracks") val nbTracks: Int = 0,
    @SerializedName("user") val user: PlaylistUser? = null
)

data class PlaylistUser(
    @SerializedName("name") val name: String = ""
)

data class PlaylistListResponse(
    @SerializedName("data") val data: List<Playlist> = emptyList()
)
