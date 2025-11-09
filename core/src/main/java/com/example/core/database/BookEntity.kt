package com.example.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
class BookEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var author: String = "",
    var description: String = ""
)