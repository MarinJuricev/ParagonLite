package com.example.data.article

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.RoomArticle

@Database(
    entities = [RoomArticle::class],
    version = 1,
    exportSchema = false
)

abstract class ArticleDataBase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDataBase::class.java,
                "article.db"
            ).build()
    }
}