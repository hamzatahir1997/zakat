package com.example.zakattracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DonationDao {

    @Query("SELECT * FROM donations ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<DonationEntry>>

    @Insert
    suspend fun insert(donation: DonationEntry): Long

    @Delete
    suspend fun delete(donation: DonationEntry)
}
