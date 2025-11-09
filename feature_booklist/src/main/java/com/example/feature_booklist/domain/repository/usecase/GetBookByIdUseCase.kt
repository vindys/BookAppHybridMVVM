package com.example.feature_booklist.domain.repository.usecase

import com.example.core.util.UiState
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(private val repository: BookRepository) {
    operator fun invoke(id: Int): Flow<UiState<Book>> = repository.getBookById(id)
}