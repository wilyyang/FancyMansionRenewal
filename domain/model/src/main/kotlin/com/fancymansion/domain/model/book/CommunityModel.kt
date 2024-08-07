package com.fancymansion.domain.model.book

data class CommunityModel(
    val publishCode: String,
    val publishTime: Long,
    val updateTime: Long,
    val downloads: Int,
    val userGoodCount: Int
)