package com.example.data.bluetooth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.data.model.RoomBluetoothEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface BluetoothDao {

    @Query("SELECT * FROM bluetooth_table")
    fun getBluetoothEntries(): Flow<List<RoomBluetoothEntry>>

    @Insert(onConflict = REPLACE)
    fun insertAll(bluetoothList: List<RoomBluetoothEntry>)

    @Query("DELETE FROM bluetooth_table")
    fun deleteAll()
}