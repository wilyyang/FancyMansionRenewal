package com.fancymansion.data.datasource.network.source.model.response

import com.fancymansion.domain.model.auth.LoginModel

data class LoginData(
    val current_user_id : String? = null,
    val login_id : String? = null,
    val name : String? = null,
    val nickname : String? = null,
    val sleep_flag : String? = null
)

fun LoginData.asModel(token : String) = LoginModel(
    currentUserId = current_user_id,
    loginId = login_id,
    name = name,
    nickname = nickname,
    isSleepAccount = sleep_flag == "Y",
    accessToken = token
)