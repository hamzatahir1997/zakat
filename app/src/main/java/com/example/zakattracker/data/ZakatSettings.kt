package com.example.zakattracker.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.zakatDataStore by preferencesDataStore(name = "zakat_settings")

/**
 * Stores the two values that must persist outside the donations list:
 * the total zakat money and a "first launch" flag used for seeding data.
 * "Remaining" and "Used" are never stored separately; they are derived.
 */
class ZakatSettings(private val context: Context) {

    private object Keys {
        val TOTAL_PAISA = longPreferencesKey("total_paisa")
        val SEEDED = booleanPreferencesKey("seeded")
    }

    val totalPaisaFlow: Flow<Long> =
        context.zakatDataStore.data.map { prefs ->
            prefs[Keys.TOTAL_PAISA] ?: SEED_TOTAL_PAISA
        }

    val seededFlow: Flow<Boolean> =
        context.zakatDataStore.data.map { prefs ->
            prefs[Keys.SEEDED] ?: false
        }

    suspend fun setTotalPaisa(value: Long) {
        context.zakatDataStore.edit { prefs ->
            prefs[Keys.TOTAL_PAISA] = value
        }
    }

    suspend fun setSeeded() {
        context.zakatDataStore.edit { prefs ->
            prefs[Keys.SEEDED] = true
        }
    }

    companion object {
        // 151,169 rupees = 15,116,900 paisa.
        const val SEED_TOTAL_PAISA = 15_116_900L
    }
}
