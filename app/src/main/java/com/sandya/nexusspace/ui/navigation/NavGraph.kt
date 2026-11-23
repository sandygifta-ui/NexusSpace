package com.sandya.nexusspace.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sandya.nexusspace.ui.screen.AlbumDetailScreen
import com.sandya.nexusspace.ui.screen.ArtistDetailScreen
import com.sandya.nexusspace.ui.screen.FavoritesScreen
import com.sandya.nexusspace.ui.screen.HomeScreen
import com.sandya.nexusspace.ui.screen.SearchScreen
import com.sandya.nexusspace.ui.screen.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object ArtistDetail : Screen("artist/{artistId}") {
        fun createRoute(artistId: Long) = "artist/$artistId"
    }
    object AlbumDetail : Screen("album/{albumId}") {
        fun createRoute(albumId: Long) = "album/$albumId"
    }
}

// Durasi animasi
private const val ANIM_DURATION = 350

// Slide dari kanan (masuk detail)
private val slideInFromRight: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(ANIM_DURATION, easing = EaseInOut)
    ) + fadeIn(animationSpec = tween(ANIM_DURATION))
}

// Slide keluar ke kiri (saat detail dibuka)
private val slideOutToLeft: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { -it / 3 },
        animationSpec = tween(ANIM_DURATION, easing = EaseInOut)
    ) + fadeOut(animationSpec = tween(ANIM_DURATION))
}

// Slide masuk dari kiri (kembali)
private val slideInFromLeft: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { -it / 3 },
        animationSpec = tween(ANIM_DURATION, easing = EaseInOut)
    ) + fadeIn(animationSpec = tween(ANIM_DURATION))
}

// Slide keluar ke kanan (kembali)
private val slideOutToRight: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(ANIM_DURATION, easing = EaseInOut)
    ) + fadeOut(animationSpec = tween(ANIM_DURATION))
}

// Fade untuk tab switching
private val fadeInTransition: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    fadeIn(animationSpec = tween(250))
}

private val fadeOutTransition: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    fadeOut(animationSpec = tween(200))
}

@Composable
fun NexusNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = { fadeInTransition() },
        exitTransition = { fadeOutTransition() },
        popEnterTransition = { fadeInTransition() },
        popExitTransition = { fadeOutTransition() }
    ) {
        // ── Tab screens — fade transition ──────────────────────────────────
        composable(
            route = Screen.Home.route,
            enterTransition = { fadeInTransition() },
            exitTransition = { fadeOutTransition() }
        ) {
            HomeScreen(
                onArtistClick = { navController.navigate(Screen.ArtistDetail.createRoute(it)) },
                onAlbumClick = { navController.navigate(Screen.AlbumDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.Search.route,
            enterTransition = { fadeInTransition() },
            exitTransition = { fadeOutTransition() }
        ) {
            SearchScreen(
                onArtistClick = { navController.navigate(Screen.ArtistDetail.createRoute(it)) },
                onAlbumClick = { navController.navigate(Screen.AlbumDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.Favorites.route,
            enterTransition = { fadeInTransition() },
            exitTransition = { fadeOutTransition() }
        ) {
            FavoritesScreen()
        }

        composable(
            route = Screen.Settings.route,
            enterTransition = { fadeInTransition() },
            exitTransition = { fadeOutTransition() }
        ) {
            SettingsScreen()
        }

        // ── Detail screens — slide transition ─────────────────────────────
        composable(
            route = Screen.ArtistDetail.route,
            arguments = listOf(navArgument("artistId") { type = NavType.LongType }),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getLong("artistId") ?: return@composable
            ArtistDetailScreen(
                artistId = artistId,
                onBack = { navController.popBackStack() },
                onAlbumClick = { navController.navigate(Screen.AlbumDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.LongType }),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getLong("albumId") ?: return@composable
            AlbumDetailScreen(
                albumId = albumId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
