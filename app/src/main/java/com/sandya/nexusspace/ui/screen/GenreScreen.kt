package com.sandya.nexusspace.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sandya.nexusspace.R
import com.sandya.nexusspace.data.model.Genre
import com.sandya.nexusspace.ui.components.ErrorMessage
import com.sandya.nexusspace.ui.components.LoadingIndicator
import com.sandya.nexusspace.ui.viewmodel.GenreViewModel

// Warna untuk tiap genre card
private val genreColors = listOf(
    Color(0xFFE53935), // Red
    Color(0xFF8E24AA), // Purple
    Color(0xFF1E88E5), // Blue
    Color(0xFF00897B), // Teal
    Color(0xFFF4511E), // Deep Orange
    Color(0xFF43A047), // Green
    Color(0xFFFF8F00), // Amber
    Color(0xFF00ACC1), // Cyan
    Color(0xFF5E35B1), // Deep Purple
    Color(0xFF039BE5), // Light Blue
    Color(0xFF7CB342), // Light Green
    Color(0xFFF6BF26), // Yellow
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreScreen(
    onGenreClick: (Genre) -> Unit,
    viewModel: GenreViewModel = viewModel()
) {
    val state by viewModel.genreState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.nav_genre),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.fillMaxSize())
            state.error != null -> ErrorMessage(
                message = state.error!!,
                modifier = Modifier.fillMaxSize()
            )
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 8.dp,
                        bottom = 100.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.genres) { genre ->
                        GenreCard(
                            genre = genre,
                            color = genreColors[state.genres.indexOf(genre) % genreColors.size],
                            onClick = { onGenreClick(genre) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenreCard(
    genre: Genre,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        // Background image
        AsyncImage(
            model = genre.pictureMedium.ifBlank { genre.picture },
            contentDescription = genre.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Color overlay gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.5f),
                            color.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Genre name
        Text(
            text = genre.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
        )
    }
}
