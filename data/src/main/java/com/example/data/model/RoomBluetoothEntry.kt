package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bluetooth_table")
data class RoomBluetoothEntry (
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val macAddress: String,
    val lastUpdated: String
)