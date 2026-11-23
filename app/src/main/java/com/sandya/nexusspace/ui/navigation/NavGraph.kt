package com.sandya.nexusspace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sandya.nexusspace.ui.screen.AlbumDetailScreen
import com.sandya.nexusspace.ui.screen.ArtistDetailScreen
import com.sandya.nexusspace.ui.screen.HomeScreen
import com.sandya.nexusspace.ui.screen.SearchScreen
import com.sandya.nexusspace.ui.screen.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object ArtistDetail : Screen("artist/{artistId}") {
        fun createRoute(artistId: Long) = "artist/$artistId"
    }
    object AlbumDetail : Screen("album/{albumId}") {
        fun createRoute(albumId: Long) = "album/$albumId"
    }
}

@Composable
fun NexusNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onArtistClick = { artistId ->
                    navController.navigate(Screen.ArtistDetail.createRoute(artistId))
                },
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onArtistClick = { artistId ->
                    navController.navigate(Screen.ArtistDetail.createRoute(artistId))
                },
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.ArtistDetail.route,
            arguments = listOf(navArgument("artistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getLong("artistId") ?: return@composable
            ArtistDetailScreen(
                artistId = artistId,
                onBack = { navController.popBackStack() },
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                }
            )
        }

        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.LongType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getLong("albumId") ?: return@composable
            AlbumDetailScreen(
                albumId = albumId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
