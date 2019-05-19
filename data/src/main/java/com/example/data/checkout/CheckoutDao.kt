package com.example.data.checkout

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.data.model.RoomCheckout

@Dao
interface CheckoutDao {

    @Query("SELECT * FROM checkout_table")
    fun getArticles(): LiveData<List<RoomCheckout>>

    //if update successful, will return number of rows effected, which should be 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(article: RoomCheckout): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg article: RoomCheckout)

    //if update successful, will return number of rows effected, which should be 1
    @Delete
    fun deleteArticle(article: RoomCheckout): Int
}