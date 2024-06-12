package com.fancymansion.core.common.wrapper

import java.util.*

data class ErrorMessage(
    val id : Long = UUID.randomUUID().mostSignificantBits,
    val message : String
)
