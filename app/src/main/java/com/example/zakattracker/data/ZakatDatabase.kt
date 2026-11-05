package com.example.zakattracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DonationEntry::class],
    version = 1,
    exportSchema = false
)
abstract class ZakatDatabase : RoomDatabase() {

    abstract fun dao(): DonationDao

    companion object {
        @Volatile
        private var INSTANCE: ZakatDatabase? = null

        fun get(context: Context): ZakatDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ZakatDatabase::class.java,
                    "zakat_tracker.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
