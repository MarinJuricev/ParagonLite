package com.example.data.checkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.RoomCheckout

@Database(
    entities = [RoomCheckout::class],
    version = 1,
    exportSchema = false
)

abstract class CheckoutDatabase : RoomDatabase() {
    abstract fun checkoutDao(): CheckoutDao

    companion object {
        @Volatile
        private var instance: CheckoutDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CheckoutDatabase::class.java,
                "checkout.db"
            ).build()
    }
}