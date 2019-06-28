package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_table")

data class RoomReceipt(
    @PrimaryKey(autoGenerate = false)
    val number: Int,
    val date: String,
    val price: Double
)

