package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.article.ArticleDao
import com.example.data.article.LocalData
import com.example.data.bluetooth.BluetoothDao
import com.example.data.checkout.CheckoutDao
import com.example.data.model.RoomArticle
import com.example.data.model.RoomBluetoothEntry
import com.example.data.model.RoomCheckout
import com.example.data.model.RoomReceipt
import com.example.data.receipt.ReceiptDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val DATABASE_NAME = "paragon_database"

@Database(
    entities = [RoomArticle::class, RoomCheckout::class, RoomBluetoothEntry::class, RoomReceipt::class],
    version = 1,
    exportSchema = false
)
abstract class ParagonDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun checkoutDao(): CheckoutDao
    abstract fun bluetoothDao(): BluetoothDao
    abstract fun receiptDao(): ReceiptDao

    companion object {
        @Volatile
        private var instance: ParagonDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ParagonDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch(Dispatchers.IO) {
                        instance
                            ?.articleDao()
                            ?.insertAll(*populateData())
                    }
                }
            }).build()

        fun populateData() = LocalData.getInitialArticles()
    }

}