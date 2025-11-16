package com.example.feature_booklist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.domain.repository.usecase.GetBookDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase
) : ViewModel(), BookDetailsViewModelContract {

    private val TAG = "BookDetailsViewModel"
    private val _bookId = MutableStateFlow<Int?>(null)
    private val _state = MutableStateFlow<UiState<Book>>(UiState.Loading)
    override val state: StateFlow<UiState<Book>> = _state.asStateFlow()

    init {
        Log.d(TAG, "ViewModel initialized: $this")
        observeBook()
    }

    private fun observeBook() = viewModelScope.launch {
        _bookId.collect { _bookId->

        }
    }

    override fun loadBook(bookId: Int) {
        viewModelScope.launch {
            Log.d(TAG, "loadBook: $bookId")
            getBookDetailsUseCase(bookId)
                .onStart {
                    Log.d(TAG, "loadBook: start id : $bookId")
                    _state.value = UiState.Loading
                }
                .catch { exception ->
                    Log.d(TAG, "loadBook: error id : $bookId")
                    _state.value = UiState.Error(message = exception.message ?: "Unknown Error")
                }
                .collect { uiState ->
                    Log.d(TAG, "loadBook: success id : $bookId")
                    _state.value = uiState
                }
        }
    }
}








