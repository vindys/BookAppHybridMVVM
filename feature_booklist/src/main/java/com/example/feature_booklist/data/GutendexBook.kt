package com.example.feature_booklist.data

data class GutendexBook(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val subjects: List<String>?
)
data class Author(
    val name: String
)
data class GutendexBookResponse(
    val results: List<GutendexBook>
)