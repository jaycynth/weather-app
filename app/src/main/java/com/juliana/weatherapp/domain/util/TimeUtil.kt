package com.juliana.weatherapp.domain.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Long.getDayOfWeekName(): String {
    val instant = Instant.ofEpochMilli(this)
    val dayOfWeek = instant.atZone(ZoneId.systemDefault()).dayOfWeek
    return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}


fun LocalDateTime.toInt(): Int = this.toEpochSecond(ZoneOffset.UTC).toInt()

fun Int.toLocalDateTime(): LocalDateTime = Instant.ofEpochSecond(this.toLong())
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()


