package com.fancymansion.core.common.util

class BookIDManager {
    companion object {
        fun generateId(existingIds: List<Long>): Long = generateId(existingIds.toSet())

        fun generateId(existingIds: Set<Long>): Long {
            for (i in 1..Long.MAX_VALUE) {
                if (i !in existingIds) return i
            }
            throw IllegalStateException("No available IDs")
        }
    }
}