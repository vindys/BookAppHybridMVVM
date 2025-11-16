package com.example.feature_booklist.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.feature_booklist.presentation.navigation.BookDestinations
import com.example.feature_booklist.presentation.ui.preview.FakeBookDetailsViewModel
import com.example.feature_booklist.presentation.ui.preview.FakeBookListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val isDetailScreen = currentRoute?.startsWith(BookDestinations.DETAILS) == true

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(if (isDetailScreen) "Book Details" else "Books") },
                navigationIcon = if (isDetailScreen) {
                    {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                } else {
                    {}
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BookDestinations.LIST,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            composable(BookDestinations.LIST) {
                BookListScreen(onNavigateToDetails = { bookId ->
                    navController.navigate("${BookDestinations.DETAILS}/$bookId")
                })
            }
            composable("${BookDestinations.DETAILS}/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
                BookDetailsScreen(bookId = bookId)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookApp() {
    BookAppPreviewContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppPreviewContent() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Books") }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BookDestinations.LIST,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            composable(BookDestinations.LIST) {
                BookListScreen(
                    onNavigateToDetails = {},
                    viewModel = FakeBookListViewModel()
                )
            }
            composable("${BookDestinations.DETAILS}/{bookId}") {
                BookDetailsScreen(
                    bookId = 1,
                    viewModel = FakeBookDetailsViewModel()
                )
            }
        }
    }
}
