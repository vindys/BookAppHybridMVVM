package com.example.feature_booklist.presentation.ui.preview

import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.presentation.viewmodel.BookDetailsViewModelContract
import com.example.feature_booklist.presentation.viewmodel.BookListViewModelContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeBookListViewModel : BookListViewModelContract {
    private val _state = MutableStateFlow<UiState<List<Book>>>(
        UiState.Success(
            listOf(
                Book(1, "1984", "George Orwell", "Dystopian novel"),
                Book(2, "Brave New World", "Aldous Huxley", "Sci-fi classic"),
                Book(3, "The Hobbit", "J.R.R. Tolkien", "Fantasy adventure")
            )
        )
    )
    override val state: StateFlow<UiState<List<Book>>> = _state
    override fun loadBooks() {}
}

class FakeBookDetailsViewModel : BookDetailsViewModelContract {
    private val _state = MutableStateFlow<UiState<Book>>(
        UiState.Success(
            Book(
                id = 1,
                title = "1984",
                author = "George Orwell",
                description = "A dystopian novel about totalitarianism."
            )
        )
    )
    override val state: StateFlow<UiState<Book>> = _state
    override fun loadBook(id: Int) {}
}
