package com.fancymansion.presentation.main.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestampLegacy(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d", Locale.getDefault())
    return formatter.format(date)
}
fun <T> List<T>.paged(
    page: Int,
    pageSize: Int
): List<T> {
    if (page < 0 || pageSize <= 0) return emptyList()

    val from = page * pageSize
    if (from >= size) return emptyList()

    val to = minOf(from + pageSize, size)
    return subList(from, to).toList()
}