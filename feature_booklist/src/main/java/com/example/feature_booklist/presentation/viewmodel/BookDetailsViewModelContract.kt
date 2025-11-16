package com.example.feature_booklist.presentation.viewmodel

import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import kotlinx.coroutines.flow.StateFlow

interface BookDetailsViewModelContract {
    val state: StateFlow<UiState<Book>>
    fun loadBook(id: Int)
}
