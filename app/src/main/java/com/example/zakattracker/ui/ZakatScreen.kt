package com.example.zakattracker.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zakattracker.ZakatViewModel
import com.example.zakattracker.ui.theme.DarkRemainingNegativeContainer
import com.example.zakattracker.ui.theme.DarkRemainingNegativeContent
import com.example.zakattracker.ui.theme.DarkRemainingPositiveContainer
import com.example.zakattracker.ui.theme.DarkRemainingPositiveContent
import com.example.zakattracker.ui.theme.LightRemainingNegativeContainer
import com.example.zakattracker.ui.theme.LightRemainingNegativeContent
import com.example.zakattracker.ui.theme.LightRemainingPositiveContainer
import com.example.zakattracker.ui.theme.LightRemainingPositiveContent
import com.example.zakattracker.util.formatRs
import com.example.zakattracker.util.parseAmountToPaisa
import com.example.zakattracker.util.parseAmountToPaisaAllowZero
import com.example.zakattracker.util.sanitizeAmountInput

private data class RemainingColors(val container: Color, val content: Color)

private fun pickRemainingColors(remainingPaisa: Long, darkTheme: Boolean): RemainingColors =
    when {
        remainingPaisa < 0 && darkTheme ->
            RemainingColors(DarkRemainingNegativeContainer, DarkRemainingNegativeContent)
        remainingPaisa < 0 ->
            RemainingColors(LightRemainingNegativeContainer, LightRemainingNegativeContent)
        darkTheme ->
            RemainingColors(DarkRemainingPositiveContainer, DarkRemainingPositiveContent)
        else ->
            RemainingColors(LightRemainingPositiveContainer, LightRemainingPositiveContent)
    }

@Composable
fun ZakatScreen(viewModel: ZakatViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.toasts.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    var showDonateDialog by rememberSaveable { mutableStateOf(false) }
    var donateAmountText by rememberSaveable { mutableStateOf("") }
    var donateHasError by rememberSaveable { mutableStateOf(false) }
    var overdraftPaisaText by rememberSaveable { mutableStateOf("") }
    var showEditTotalDialog by rememberSaveable { mutableStateOf(false) }
    var editTotalText by rememberSaveable { mutableStateOf("") }
    var editTotalHasError by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Take the LARGEST navigation-related inset from the system (nav bars,
            // tappable system area, and gesture zones) so the button stays clear of
            // the gesture strip on every phone, Samsung One UI included.
            val bottomBarInsets = WindowInsets.navigationBars
                .union(WindowInsets.tappableElement)
                .union(WindowInsets.systemGestures)
                .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(bottomBarInsets)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
                Button(
                    onClick = {
                        donateAmountText = ""
                        donateHasError = false
                        showDonateDialog = true
                    },
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Donate", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Zakat Tracker",
                    style = MaterialTheme.typography.headlineMedium
                        .copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(22.dp))

                val remainingPaisa = state.totalPaisa - state.usedPaisa
                val remainingPalette =
                    pickRemainingColors(remainingPaisa, isSystemInDarkTheme())
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = remainingPalette.container)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Remaining",
                            color = remainingPalette.content,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = formatRs(remainingPaisa),
                            color = remainingPalette.content,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Total − Used, calculated automatically",
                            color = remainingPalette.content.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ElevatedCard(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 12.dp, end = 6.dp, bottom = 12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Total Zakat Money",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatRs(state.totalPaisa),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(
                                onClick = {
                                    editTotalText = ""
                                    editTotalHasError = false
                                    showEditTotalDialog = true
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit Total Zakat Money",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                    ElevatedCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Used",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatRs(state.usedPaisa),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Past Donations",
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${state.donations.size} entries",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (state.donations.isEmpty()) {
                    Text(
                        text = "No donations yet. Press the Donate button to add your first one.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 18.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        state.donations.forEach { donation ->
                            DonationRow(
                                donation = donation,
                                onDelete = { viewModel.deleteDonation(donation) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showDonateDialog) {
        AlertDialog(
            onDismissRequest = {
                showDonateDialog = false
                donateHasError = false
            },
            title = { Text(text = "Enter Donation Amount") },
            text = {
                OutlinedTextField(
                    value = donateAmountText,
                    onValueChange = {
                        donateAmountText = sanitizeAmountInput(it)
                        donateHasError = false
                    },
                    label = { Text(text = "Amount (Rs.)") },
                    singleLine = true,
                    isError = donateHasError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    supportingText = {
                        Text(
                            text = if (donateHasError) {
                                "Please enter a valid amount greater than zero"
                            } else {
                                "Numbers only, for example 500 or 1250.50"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val parsed = parseAmountToPaisa(donateAmountText)
                    if (parsed == null) {
                        donateHasError = true
                    } else {
                        val remainingNow = state.totalPaisa - state.usedPaisa
                        if (parsed > remainingNow) {
                            overdraftPaisaText = parsed.toString()
                        } else {
                            viewModel.donate(parsed)
                            showDonateDialog = false
                            donateAmountText = ""
                            donateHasError = false
                        }
                    }
                }) { Text(text = "Confirm") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDonateDialog = false
                    donateHasError = false
                }) { Text(text = "Cancel") }
            }
        )
    }

    val overdraftPaisa = overdraftPaisaText.toLongOrNull()
    if (overdraftPaisa != null) {
        AlertDialog(
            onDismissRequest = { overdraftPaisaText = "" },
            title = { Text(text = "Not enough balance") },
            text = {
                Text(
                    text = "This donation of " + formatRs(overdraftPaisa) +
                        " exceeds your remaining balance of " +
                        formatRs(state.totalPaisa - state.usedPaisa) +
                        ".\n\nContinue anyway? Your balance is allowed to go below zero."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.donate(overdraftPaisa)
                    overdraftPaisaText = ""
                    showDonateDialog = false
                    donateAmountText = ""
                    donateHasError = false
                }) { Text(text = "Continue") }
            },
            dismissButton = {
                TextButton(onClick = { overdraftPaisaText = "" }) { Text(text = "Go back") }
            }
        )
    }

    if (showEditTotalDialog) {
        AlertDialog(
            onDismissRequest = {
                showEditTotalDialog = false
                editTotalHasError = false
            },
            title = { Text(text = "Edit Total Zakat Money") },
            text = {
                OutlinedTextField(
                    value = editTotalText,
                    onValueChange = {
                        editTotalText = sanitizeAmountInput(it)
                        editTotalHasError = false
                    },
                    label = { Text(text = "New total (Rs.)") },
                    singleLine = true,
                    isError = editTotalHasError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    supportingText = {
                        Text(
                            text = if (editTotalHasError) {
                                "Please enter a valid amount of zero or more"
                            } else {
                                "This is your full zakat pool, not your remaining balance"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val parsed = parseAmountToPaisaAllowZero(editTotalText)
                    if (parsed == null) {
                        editTotalHasError = true
                    } else {
                        viewModel.setTotal(parsed)
                        showEditTotalDialog = false
                        editTotalText = ""
                        editTotalHasError = false
                    }
                }) { Text(text = "Save") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEditTotalDialog = false
                    editTotalHasError = false
                }) { Text(text = "Cancel") }
            }
        )
    }
}
