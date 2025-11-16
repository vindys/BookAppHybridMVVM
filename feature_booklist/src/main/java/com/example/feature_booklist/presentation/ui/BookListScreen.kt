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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.presentation.ui.preview.FakeBookListViewModel
import com.example.feature_booklist.presentation.viewmodel.BookListViewModel
import com.example.feature_booklist.presentation.viewmodel.BookListViewModelContract

@Composable
fun BookListScreen(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: BookListViewModelContract? = null
) {
    val actualViewModel = viewModel ?: hiltViewModel<BookListViewModel>()
    val uiState by actualViewModel.state.collectAsStateWithLifecycle()

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
                Button(onClick = { viewModel?.loadBooks() }) {
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

@Preview(showBackground = true)
@Composable
fun PreviewBookListScreen() {
    BookListScreen(
        onNavigateToDetails = {},
        viewModel = FakeBookListViewModel()
    )
}


