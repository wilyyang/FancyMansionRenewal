package com.fancymansion.core.common.util

inline fun <R> String.ifNotBlank(value : (String) -> R) : R? =
    if (this.isNotBlank()) value(this) else null