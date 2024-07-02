package com.fancymansion.core.common.util

import java.io.File

inline fun <R> String.ifNotBlank(value : (String) -> R) : R? =
    if (this.isNotBlank()) value(this) else null

fun String.joinPath(other: String): String {
    return this + File.separator + other
}