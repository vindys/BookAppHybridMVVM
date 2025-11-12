package com.example.feature_booklist.presentation.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.presentation.viewmodel.BookListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    navController: NavController,
    viewModel: BookListViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Books") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxWidth())
        {
            when (uiState) {
                is UiState.Loading -> {

                    CircularProgressIndicator()

                }

                is UiState.Error -> {
                    val message = (uiState as UiState.Error).message
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                        Log.d("BookListScreen", "Clicked book id: ${book.id}")
                                        navController.navigate("book_details/${book.id}")
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

