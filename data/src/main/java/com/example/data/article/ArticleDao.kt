package com.example.data.article

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.RoomArticle

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_table")
    fun getArticles(): LiveData<List<RoomArticle>>

    //if update successful, will return number of rows effected, which should be 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(article: RoomArticle): Long

//    @Query("DELETE from article_table where name(name) = name(:name)")
//    fun deleteArticle(name: String): LiveData<List<RoomArticle>>
}