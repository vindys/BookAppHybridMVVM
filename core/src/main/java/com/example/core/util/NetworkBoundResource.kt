package com.example.core.util

import kotlinx.coroutines.flow.*

inline fun <ResultType: Any, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType?>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true }
): Flow<UiState<ResultType>> = flow {
    emit(UiState.Loading)

    query()
        .collect { cached ->
            val fetchNeeded = cached == null || shouldFetch(cached)
            if (fetchNeeded) {
        try {
            val remote = fetch()
            saveFetchResult(remote)
                    // After saving, emit latest from DB
            query()
                .filterNotNull()
                        .collect { emit(UiState.Success(it)) }
                } catch (e: Throwable) {
                    emit(UiState.Error(e.message ?: "Network error"))
        }
    } else {
                if (cached != null) emit(UiState.Success(cached))
            }
    }
}

