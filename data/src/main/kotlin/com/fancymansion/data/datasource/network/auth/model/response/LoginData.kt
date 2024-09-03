package com.fancymansion.data.datasource.network.auth.model.response

data class LoginData(
    val current_user_id : String? = null,
    val login_id : String? = null,
    val name : String? = null,
    val nickname : String? = null,
    val sleep_flag : String? = null
)