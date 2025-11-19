package com.example.feature_booklist.domain.repository

import com.example.core.database.BookDao
import com.example.core.util.UiState
import com.example.core.util.networkBoundListResource
import com.example.core.util.networkBoundResource
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.data.BookApi
import com.example.feature_booklist.data.toBook
import com.example.feature_booklist.data.toDomain
import com.example.feature_booklist.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: BookApi,
    private val dao: BookDao
) : BookRepository {

    override fun getBooks(): Flow<UiState<List<Book>>> =
        networkBoundListResource(
            query = {
                dao.getBooks().map { entities -> entities.map { it.toDomain() } }
            },
            fetch = {
                api.getBooks().results
            },
            saveFetchResult = { remoteBooks ->
                    dao.insertAll(remoteBooks.map { it.toBook().toEntity() })
            },
            shouldFetch = { cached -> cached.isEmpty() } // only if no data
        )

    override fun getBookById(id: Int): Flow<UiState<Book>> =
        networkBoundResource(
        query = { dao.getBookById(id).map { it?.toDomain() } },
        fetch = { api.getBookById(id) },
            saveFetchResult = { remote ->
                        dao.insertAll(listOf(remote.toBook().toEntity()))
            },
        shouldFetch = { cached -> cached == null } // only if not in DB
        )
}
