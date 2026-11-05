package com.example.zakattracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One past donation. Money is stored in paisa (1 rupee = 100 paisa) so that
 * decimal amounts are handled with exact integer math, never floating point.
 */
@Entity(tableName = "donations")
data class DonationEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amountPaisa: Long,
    val createdAt: Long
)
