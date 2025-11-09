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
) : ViewModel() {

    private val TAG = "BookDetailsViewModel"
    private val _bookId = MutableStateFlow<Int?>(null)
    private val _state = MutableStateFlow<UiState<Book>>(UiState.Loading)
    val state: StateFlow<UiState<Book>> = _state.asStateFlow()

    init {
        Log.d(TAG, "ViewModel initialized: $this")
        observeBook()
    }

    private fun observeBook() {

            _bookId
                .filterNotNull()
                .distinctUntilChanged()
                .onEach { Log.d(TAG, "bookId emitted: $it") }
                .flatMapLatest { id ->
                    Log.d(TAG, "flatMapLatest triggered for ID: $id")
                    getBookDetailsUseCase(id)
                        .catch { e ->
                            _state.value = UiState.Error(e.message ?: "Unknown error")
                        }
                }
                .onEach { result ->
                    _state.value = result
                }
                .launchIn(viewModelScope)
        }
    fun loadBook(id: Int) {
        Log.d(TAG, "loadBook: $id")
        _bookId.value = id
    }
    }








