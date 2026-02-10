package com.fancymansion.core.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestampDateTime(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d a h시 m분", Locale.getDefault())
    return formatter.format(date)
}