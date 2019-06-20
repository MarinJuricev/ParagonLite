package com.example.data.bluetooth

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.RoomBluetoothEntry

@Database(
    entities = [RoomBluetoothEntry::class],
    version = 1,
    exportSchema = false
)

abstract class BluetoothDatabase : RoomDatabase() {
    abstract fun bluetoothDao(): BluetoothDao

    companion object {
        @Volatile
        private var instance: BluetoothDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BluetoothDatabase::class.java,
                "bluetooth.db"
            ).build()
    }
}