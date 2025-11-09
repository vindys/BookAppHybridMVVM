package com.example.core.util

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
): Flow<UiState<ResultType>> = flow {
    emit(UiState.Loading)

    val data = query().firstOrNull()

    val flow = if (data == null || shouldFetch(data)) {
        try {
            val remote = fetch()
            saveFetchResult(remote)
            query().map { UiState.Success(it) }
        } catch (throwable: Throwable) {
            query().map {
                UiState.Error(throwable.message ?: "Network error")
            }
        }
    } else {
        query().map { UiState.Success(it) }
    }

    emitAll(flow)
}
