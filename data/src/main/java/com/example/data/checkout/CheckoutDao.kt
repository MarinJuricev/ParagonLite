package com.example.data.checkout

import androidx.room.*
import com.example.data.model.RoomCheckout
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckoutDao {

    @Query("SELECT * FROM checkout_table")
    fun getArticles(): Flow<List<RoomCheckout>>

    @Query("SELECT * FROM checkout_table WHERE name= :articleName")
    fun getArticle(articleName: String): RoomCheckout?

    @Query("SELECT SUM(inCheckout) FROM checkout_table ")
    fun getCheckoutArticleCount(): Flow<Int>

    //if update successful, will return number of rows effected, which should be 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(article: RoomCheckout): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg article: RoomCheckout)

    //if update successful, will return number of rows effected, which should be 1
    @Delete
    fun deleteArticle(article: RoomCheckout): Int

    @Query("DELETE FROM checkout_table")
    fun deleteAll()
}