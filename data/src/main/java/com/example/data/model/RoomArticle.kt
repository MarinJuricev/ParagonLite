package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_table")

data class RoomArticle(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val quantity: String,
    val price: Double
)

