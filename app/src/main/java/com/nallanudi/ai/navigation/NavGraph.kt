package com.nallanudi.ai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nallanudi.ai.presentation.screens.*
import com.nallanudi.ai.presentation.viewmodel.MainViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Detail : Screen("detail/{termId}") {
        fun createRoute(termId: Long) = "detail/$termId"
    }
    object Favorites : Screen("favorites")
    object Flashcards : Screen("flashcards")
    object Chat : Screen("chat?termId={termId}") {
        fun createRoute(termId: Long?) = if (termId != null) "chat?termId=$termId" else "chat"
    }
    object Quiz : Screen("quiz")
}

@Composable
fun NavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onAnimationFinished = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(viewModel, navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("termId") { type = NavType.LongType })
        ) { backStackEntry ->
            val termId = backStackEntry.arguments?.getLong("termId") ?: return@composable
            DetailScreen(termId, viewModel, navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(viewModel, navController)
        }
        composable(Screen.Flashcards.route) {
            FlashcardScreen(viewModel, navController)
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("termId") { type = NavType.StringType; nullable = true; defaultValue = null })
        ) { backStackEntry ->
            val termIdStr = backStackEntry.arguments?.getString("termId")
            val termId = termIdStr?.toLongOrNull()
            ChatScreen(termId, viewModel, navController)
        }
        composable(Screen.Quiz.route) {
            QuizScreen(viewModel, navController)
        }
    }
}
