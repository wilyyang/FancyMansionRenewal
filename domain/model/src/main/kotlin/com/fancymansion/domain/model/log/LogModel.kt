package com.fancymansion.domain.model.log

data class LogModel(
    val tag: String,
    val type: Int,
    val message: String,
    val timeSaved: String
)