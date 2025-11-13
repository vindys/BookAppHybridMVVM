package com.example.feature_booklist.presentation.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.presentation.navigation.BookDestinations
import com.example.feature_booklist.presentation.viewmodel.BookDetailsViewModel
import com.example.feature_booklist.presentation.viewmodel.BookListViewModel

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
                BookListContent(onNavigateToDetails = { bookId ->
                    navController.navigate("${BookDestinations.DETAILS}/$bookId")
                })
            }
            composable("${BookDestinations.DETAILS}/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
                BookDetailsContent(bookId = bookId)
            }
        }
    }
}

@Composable
fun BookListContent(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: BookListViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is UiState.Error -> {
            val message = (uiState as UiState.Error).message
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Error: $message", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadBooks() }) {
                    Text("Retry")
                }
            }
        }

        is UiState.Success -> {
            val books = (uiState as UiState.Success<List<Book>>).data
            LazyColumn {
                items(books) { book ->
                    Text(
                        text = "${book.title} by ${book.author}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                Log.d("BookList", "Clicked: ${book.id}")
                                onNavigateToDetails(book.id)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun BookDetailsContent(
    bookId: Int,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    when (state) {
        is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is UiState.Error -> {
            val message = (state as UiState.Error).message
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Error: $message", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadBook(bookId) }) {
                    Text("Retry")
                }
            }
        }

        is UiState.Success<*> -> {
            val book = (state as UiState.Success<Book>).data
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(book.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Author: ${book.author}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(book.description, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
