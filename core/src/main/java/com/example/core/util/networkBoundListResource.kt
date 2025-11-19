package com.example.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <T : Any, RequestType> networkBoundListResource(
    crossinline query: () -> Flow<List<T>>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (List<T>) -> Boolean = { true }
): Flow<UiState<List<T>>> = flow {

    emit(UiState.Loading)

    val current = query().first()

    val flow = if (shouldFetch(current)) {
        try {
            val remote = fetch()
            saveFetchResult(remote)
            query().map { UiState.Success(it) }
        } catch (e: Throwable) {
            query().map { UiState.Error(e.message ?: "Network error") }
        }
    } else {
        query().map { UiState.Success(it) }
    }

    emitAll(flow)
}
