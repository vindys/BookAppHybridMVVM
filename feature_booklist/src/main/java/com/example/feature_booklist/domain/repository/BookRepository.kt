package com.example.feature_booklist.domain.repository

import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooks(): Flow<UiState<List<Book>>>
    fun getBookById(id: Int): Flow<UiState<Book>>
}