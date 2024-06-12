package com.fancymansion.domain.model.auth

data class LoginModel(
    val currentUserId : String? = null,
    val loginId : String? = null,
    val name : String? = null,
    val nickname : String? = null,
    val isSleepAccount : Boolean = false,
    val accessToken : String? = null
)