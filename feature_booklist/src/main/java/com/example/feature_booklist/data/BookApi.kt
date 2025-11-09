package com.example.feature_booklist.data

import androidx.room.Query
import retrofit2.http.GET
import retrofit2.http.Path

interface BookApi {
    @GET("books")
    suspend fun getBooks(
    ): GutendexBookResponse

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: Int): GutendexBook


}