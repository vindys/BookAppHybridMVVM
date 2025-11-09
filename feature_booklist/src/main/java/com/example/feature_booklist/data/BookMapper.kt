package com.example.feature_booklist.data

import com.example.core.database.BookEntity

fun GutendexBook.toBook(): Book = Book(
    id = id,
    title = title,
    author = authors.joinToString(", ")  { it.name },
    description = subjects?.joinToString(", ") ?: "No Description Available"
)

// Entity -> Domain
fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    author = author,
    description = description
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    author = author,
    description = description
)
