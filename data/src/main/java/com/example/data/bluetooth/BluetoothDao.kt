package com.example.data.bluetooth

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.data.model.RoomBluetoothEntry

@Dao
interface BluetoothDao {

    @Query("SELECT * FROM bluetooth_table")
    fun getBluetoothEntries(): LiveData<List<RoomBluetoothEntry>>

    @Insert(onConflict = REPLACE)
    fun insertAll(bluetoothList: List<RoomBluetoothEntry>)

    @Query("DELETE FROM bluetooth_table")
    fun deleteAll()
}