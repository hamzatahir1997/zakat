package com.example.zakattracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zakattracker.data.DonationEntry
import com.example.zakattracker.data.ZakatDatabase
import com.example.zakattracker.data.ZakatSettings
import com.example.zakattracker.util.formatRs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Everything the main screen needs to draw itself. "Used" and the donations
 * list always come from the database, so deleting an old donation instantly
 * reduces "Used" and raises "Remaining".
 */
data class ZakatUiState(
    val isLoading: Boolean = true,
    val totalPaisa: Long = 0L,
    val usedPaisa: Long = 0L,
    val donations: List<DonationEntry> = emptyList()
)

class ZakatViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ZakatDatabase.get(application).dao()
    private val settings = ZakatSettings(application)

    private val _toasts = MutableSharedFlow<String>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toasts: SharedFlow<String> = _toasts

    val uiState: StateFlow<ZakatUiState> = combine(
        dao.observeAll(),
        settings.totalPaisaFlow,
        settings.seededFlow
    ) { donations, totalPaisa, seeded ->
        if (!seeded) {
            ZakatUiState(isLoading = true)
        } else {
            ZakatUiState(
                isLoading = false,
                totalPaisa = totalPaisa,
                usedPaisa = donations.sumOf { it.amountPaisa },
                donations = donations
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ZakatUiState()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) { seedIfFirstLaunch() }
    }

    fun donate(amountPaisa: Long) {
        viewModelScope.launch {
            dao.insert(
                DonationEntry(
                    amountPaisa = amountPaisa,
                    createdAt = System.currentTimeMillis()
                )
            )
            _toasts.emit(formatRs(amountPaisa) + " donated")
        }
    }

    fun deleteDonation(donation: DonationEntry) {
        viewModelScope.launch {
            dao.delete(donation)
            _toasts.emit("Donation deleted")
        }
    }

    fun setTotal(totalPaisa: Long) {
        viewModelScope.launch {
            settings.setTotalPaisa(totalPaisa)
            _toasts.emit("Total Zakat Money updated")
        }
    }

    /**
     * First launch only: loads the starting figures so the ledger opens with
     * Total 151,169 and Used 140,145 (four pre-loaded donations), which makes
     * Remaining exactly 11,024 — calculated, never stored.
     */
    private suspend fun seedIfFirstLaunch() {
        if (settings.seededFlow.first()) return
        val now = System.currentTimeMillis()
        val dayMillis = 24L * 60 * 60 * 1000
        val seedDonations = listOf(
            8_514_500L to now - 62 * dayMillis,  // Rs. 85,145
            3_000_000L to now - 40 * dayMillis,  // Rs. 30,000
            2_500_000L to now - 9 * dayMillis    // Rs. 25,000
        )
        seedDonations.forEach { (amountPaisa, createdAt) ->
            dao.insert(DonationEntry(amountPaisa = amountPaisa, createdAt = createdAt))
        }
        settings.setTotalPaisa(ZakatSettings.SEED_TOTAL_PAISA)
        settings.setSeeded()
    }
}
