package com.example.data.article

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.data.model.RoomArticle

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_table")
    fun getArticles(): LiveData<List<RoomArticle>>

    //if update successful, will return number of rows effected, which should be 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(article: RoomArticle): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg article: RoomArticle)

    //if update successful, will return number of rows effected, which should be 1
    @Delete
    fun deleteArticle(article: RoomArticle): Int
}