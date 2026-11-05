package com.example.zakattracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToLong

/**
 * Converts text a user typed (for example "500" or "1250.50") into paisa.
 * Returns null when the input is not a positive amount of money.
 */
fun parseAmountToPaisa(input: String): Long? {
    val cleaned = input.trim().replace(",", "").replace(" ", "")
    if (cleaned.isEmpty()) return null
    val value = cleaned.toDoubleOrNull() ?: return null
    if (!value.isFinite()) return null
    if (value <= 0) return null
    if (value > 99_999_999_999.99) return null
    return (value * 100).roundToLong()
}

/**
 * Formats a paisa amount as display text, for example:
 * 15_116_900 -> "Rs. 151,169" and 50_050 -> "Rs. 500.50".
 * Negative balances show as "Rs. -1,000" (never stored, only calculated).
 */
fun formatRs(paisa: Long): String {
    val negative = paisa < 0
    val absolute = Math.abs(paisa)
    val whole = absolute / 100
    val fraction = absolute % 100
    val number = if (fraction == 0L) {
        String.format(Locale.US, "%,d", whole)
    } else {
        String.format(Locale.US, "%,d.%02d", whole, fraction)
    }
    return if (negative) "Rs. -$number" else "Rs. $number"
}

/**
 * Same as parseAmountToPaisa but also accepts zero, used when setting the
 * total zakat money (zero is a legitimate total).
 */
fun parseAmountToPaisaAllowZero(input: String): Long? {
    val cleaned = input.trim().replace(",", "").replace(" ", "")
    if (cleaned.isEmpty()) return null
    val value = cleaned.toDoubleOrNull() ?: return null
    if (!value.isFinite()) return null
    if (value < 0) return null
    if (value == 0.0) return 0L
    if (value > 99_999_999_999.99) return null
    return (value * 100).roundToLong()
}

/**
 * Keeps only digits and one decimal dot, so the amount field stays valid
 * no matter what else users paste or type.
 */
fun sanitizeAmountInput(raw: String): String {
    val builder = StringBuilder()
    var sawDot = false
    for (character in raw) {
        if (character.isDigit()) {
            builder.append(character)
            if (builder.length >= 13) break
        } else if (character == '.' && !sawDot) {
            sawDot = true
            builder.append('.')
        }
    }
    return builder.toString()
}

private val dateFormat = SimpleDateFormat("d MMM yyyy, h:mm a", Locale.US)

fun formatTimestamp(epochMillis: Long): String = dateFormat.format(Date(epochMillis))
