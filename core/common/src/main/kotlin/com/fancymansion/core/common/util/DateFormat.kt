package com.fancymansion.core.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestampYearDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d", Locale.getDefault())
    return formatter.format(date)
}
fun formatTimestampDateTime(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d a h시 m분", Locale.getDefault())
    return formatter.format(date)
}

fun formatTimestampOnlyDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("M.d", Locale.getDefault())
    return formatter.format(date)
}