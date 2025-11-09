package com.example.feature_booklist.domain.repository.usecase

import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBooksUseCase(private val repository: BookRepository) {
    operator fun invoke(): Flow<UiState<List<Book>>> = repository.getBooks()
}