package com.example.data.receipt

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.RoomReceipt

@Dao
interface ReceiptDao {

    @Query("SELECT * FROM receipt_table WHERE date BETWEEN :startDate AND :endDate")
    fun getReceipts(startDate: String, endDate: String): LiveData<List<RoomReceipt>>

    //if update successful, will return number of rows effected, which should be 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(article: RoomReceipt): Long

}