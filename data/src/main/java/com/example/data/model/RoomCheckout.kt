package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checkout_table")

data class RoomCheckout(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val quantity: String,
    val price: Double,
    val inCheckout: Int
)

