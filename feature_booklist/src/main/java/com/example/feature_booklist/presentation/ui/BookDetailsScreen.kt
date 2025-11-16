package com.example.feature_booklist.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.presentation.ui.preview.FakeBookDetailsViewModel
import com.example.feature_booklist.presentation.viewmodel.BookDetailsViewModel
import com.example.feature_booklist.presentation.viewmodel.BookDetailsViewModelContract


@Composable
fun BookDetailsScreen(
    bookId: Int,
    viewModel: BookDetailsViewModelContract? = null
) {
    val actualViewModel = viewModel ?: hiltViewModel<BookDetailsViewModel>()
    val state by actualViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(bookId) {
        actualViewModel.loadBook(bookId)
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
                Button(onClick = { viewModel?.loadBook(bookId) }) {
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


@Preview(showBackground = true)
@Composable
fun PreviewBookDetailsScreen() {
    BookDetailsScreen(
        bookId = 1,
        viewModel = FakeBookDetailsViewModel()
    )
}
