package com.example.feature_booklist.presentation.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.presentation.viewmodel.BookDetailsViewModel
import com.example.feature_booklist.presentation.viewmodel.BookListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val isDetailScreen = currentRoute?.startsWith("book_details") == true

    Scaffold(
        topBar = {
            if (isDetailScreen) {
                SmallTopAppBar(
                    title = { Text("Book Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            } else {
                SmallTopAppBar(
                    title = { Text("Books") }
                )
            }
        }
    )
    { padding ->
        NavHost(
            navController = navController,
            startDestination = "book_list",
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            composable("book_list") {
                BookListContent(navController)
            }
            composable("book_details/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
                BookDetailsContent(bookId = bookId)
            }
        }
    }
}

@Composable
fun BookListContent(
    navController: NavHostController,
    viewModel: BookListViewModel = hiltViewModel()
){
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
                                navController.navigate("book_details/${book.id}")
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
){
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
