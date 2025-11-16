package com.example.feature_booklist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.domain.repository.usecase.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/*data class BookListState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)*/

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase
) : ViewModel(), BookListViewModelContract {
    private val _state = MutableStateFlow<UiState<List<Book>>>(UiState.Loading)
    override val state: StateFlow<UiState<List<Book>>> = _state

    init {
        loadBooks()
    }

    override fun loadBooks() {
        viewModelScope.launch {
            getBooksUseCase().collect { _state.value = it }
                }
        }
    }
