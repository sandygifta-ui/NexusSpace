package com.sandya.nexusspace.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sandya.nexusspace.ui.components.formatDuration
import com.sandya.nexusspace.ui.favorites.FavoritesManager
import com.sandya.nexusspace.ui.player.MusicPlayerManager
import com.sandya.nexusspace.ui.theme.NexusGreen

@Composable
fun NowPlayingScreen(onDismiss: () -> Unit) {
    val track by MusicPlayerManager.currentTrack.collectAsState()
    val isPlaying by MusicPlayerManager.isPlaying.collectAsState()
    val isLoading by MusicPlayerManager.isLoading.collectAsState()
    val progress by MusicPlayerManager.progress.collectAsState()
    val elapsed by MusicPlayerManager.elapsedSeconds.collectAsState()
    val total by MusicPlayerManager.totalSeconds.collectAsState()
    val favList by FavoritesManager.favorites.collectAsState()
    val isFavorite = track?.let { t -> favList.any { it.id == t.id } } ?: false

    // Rotating animation
    val infiniteTransition = rememberInfiniteTransition(label = "rotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "albumRotation"
    )

    track?.let { t ->
        // Full opaque background — tidak transparan
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // ── Top Bar ────────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Tutup",
                            modifier = Modifier.size(34.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SEDANG DIPUTAR",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                    }
                    IconButton(onClick = { FavoritesManager.toggleFavorite(t) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite
                                          else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorit",
                            tint = if (isFavorite) Color(0xFFE53935)
                                   else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // ── Album Art (Vinyl) ──────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = CircleShape,
                            ambientColor = NexusGreen.copy(alpha = 0.3f),
                            spotColor = NexusGreen.copy(alpha = 0.5f)
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = t.album.coverXl.ifBlank { t.album.coverMedium },
                        contentDescription = t.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(if (isPlaying) rotation else 0f)
                    )
                    // Vinyl hole
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                // ── Track Info ─────────────────────────────────────────────
                Text(
                    text = t.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = t.artist.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = NexusGreen,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Progress Slider ────────────────────────────────────────
                Slider(
                    value = progress,
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = NexusGreen,
                        activeTrackColor = NexusGreen,
                        inactiveTrackColor = NexusGreen.copy(alpha = 0.2f)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(elapsed),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatDuration(total),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ── Controls ───────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous
                    IconButton(
                        onClick = { MusicPlayerManager.skipPrevious() },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            Icons.Default.SkipPrevious,
                            contentDescription = "Sebelumnya",
                            modifier = Modifier.size(42.dp),
                            tint = if (MusicPlayerManager.hasPrevious())
                                MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }

                    // Play/Pause
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(NexusGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            isLoading -> CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                            isPlaying -> IconButton(
                                onClick = { MusicPlayerManager.playOrPause(t.id, t.preview, t) }
                            ) {
                                Icon(
                                    Icons.Default.Pause,
                                    contentDescription = "Pause",
                                    modifier = Modifier.size(38.dp),
                                    tint = Color.White
                                )
                            }
                            else -> IconButton(
                                onClick = { MusicPlayerManager.playOrPause(t.id, t.preview, t) }
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    modifier = Modifier.size(38.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Next
                    IconButton(
                        onClick = { MusicPlayerManager.skipNext() },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            Icons.Default.SkipNext,
                            contentDescription = "Berikutnya",
                            modifier = Modifier.size(42.dp),
                            tint = if (MusicPlayerManager.hasNext())
                                MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Preview 30 detik • Deezer API",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}
