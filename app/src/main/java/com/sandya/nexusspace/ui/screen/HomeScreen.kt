package com.sandya.nexusspace.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sandya.nexusspace.R
import com.sandya.nexusspace.ui.components.AlbumCard
import com.sandya.nexusspace.ui.components.ArtistCard
import com.sandya.nexusspace.ui.components.ErrorMessage
import com.sandya.nexusspace.ui.components.LoadingIndicator
import com.sandya.nexusspace.ui.components.SectionHeader
import com.sandya.nexusspace.ui.components.TrackCard
import com.sandya.nexusspace.ui.player.MusicPlayerManager
import com.sandya.nexusspace.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onArtistClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            actions = {
                IconButton(onClick = { viewModel.loadChart() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.refresh),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        when {
            uiState.isLoading && uiState.trendingTracks.isEmpty() -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
            uiState.error != null && uiState.trendingTracks.isEmpty() -> {
                ErrorMessage(message = uiState.error!!, modifier = Modifier.fillMaxSize())
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // ── Hero Banner ────────────────────────────────────────
                    if (uiState.trendingTracks.isNotEmpty()) {
                        val featured = uiState.trendingTracks.first()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp)
                        ) {
                            AsyncImage(
                                model = featured.album.coverXl.ifBlank { featured.album.coverMedium },
                                contentDescription = featured.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.1f),
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                                                MaterialTheme.colorScheme.background
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.featured_track),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = featured.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = featured.artist.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        MusicPlayerManager.playOrPause(
                                            featured.id,
                                            featured.preview,
                                            featured
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = stringResource(R.string.play_now),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // ── Trending Tracks ────────────────────────────────────
                    SectionHeader(title = stringResource(R.string.trending_tracks))
                    uiState.trendingTracks.forEachIndexed { index, track ->
                        TrackCard(track = track, index = index + 1)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Popular Albums ─────────────────────────────────────
                    SectionHeader(title = stringResource(R.string.popular_albums))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        uiState.trendingAlbums.forEach { album ->
                            AlbumCard(album = album, onClick = { onAlbumClick(album.id) })
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Top Artists ────────────────────────────────────────
                    SectionHeader(title = stringResource(R.string.top_artists))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        uiState.trendingArtists.forEach { artist ->
                            ArtistCard(artist = artist, onClick = { onArtistClick(artist.id) })
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}
