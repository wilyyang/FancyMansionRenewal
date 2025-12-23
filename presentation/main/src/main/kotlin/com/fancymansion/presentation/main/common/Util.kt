package com.fancymansion.presentation.main.common

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