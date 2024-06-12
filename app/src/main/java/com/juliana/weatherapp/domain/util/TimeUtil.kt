package com.juliana.weatherapp.domain.util

import androidx.compose.ui.text.toLowerCase
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Long.getDayOfWeekName(): String {
    val instant = Instant.ofEpochMilli(this)
    val dayOfWeek = instant.atZone(ZoneId.systemDefault()).dayOfWeek
    return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}


fun LocalDateTime.toInt(): Int = this.toEpochSecond(ZoneOffset.UTC).toInt()

fun Int.toLocalDateTime(): LocalDateTime = Instant.ofEpochSecond(this.toLong())
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

fun getDayOfWeek(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

    val dateTime = LocalDateTime.parse(dateString, formatter)

    val dayOfWeek = dateTime.dayOfWeek

    return dayOfWeek.name.lowercase(Locale.ROOT)
}


