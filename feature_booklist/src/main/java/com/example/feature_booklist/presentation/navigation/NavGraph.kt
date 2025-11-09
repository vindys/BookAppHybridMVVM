package com.example.feature_booklist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
//import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature_booklist.presentation.ui.BookDetailsScreen
import com.example.feature_booklist.presentation.ui.BookListScreen
import com.example.feature_booklist.presentation.viewmodel.BookDetailsViewModel


@Composable
fun BookNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "book_list"
    ) {
        composable("book_list") {
            BookListScreen(navController)
        }

        // Details screen
        composable(
            "book_details/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
            val viewModel: BookDetailsViewModel = hiltViewModel(backStackEntry)
            BookDetailsScreen(bookId = bookId, navController = navController, viewModel = viewModel)
        }
    }
}