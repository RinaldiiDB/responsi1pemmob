package com.pemmob.responsi1pemmob.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.responsi1pemmob.ui.screen.DetailScreen
import com.pemmob.responsi1pemmob.ui.screen.HomeScreen
import com.pemmob.responsi1pemmob.ui.viewmodel.AnimeDetailViewModel
import com.pemmob.responsi1pemmob.ui.viewmodel.AnimeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Int) = "detail/$animeId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val animeViewModel: AnimeViewModel = viewModel()
            HomeScreen(
                viewModel = animeViewModel,
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("animeId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
            val detailViewModel: AnimeDetailViewModel = viewModel()
            DetailScreen(
                animeId = animeId,
                viewModel = detailViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
