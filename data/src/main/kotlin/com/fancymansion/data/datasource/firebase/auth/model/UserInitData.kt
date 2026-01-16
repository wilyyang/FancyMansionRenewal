package com.fancymansion.data.datasource.firebase.auth.model

import com.fancymansion.domain.model.user.UserInitModel

data class UserInitData(
    val uid: String,
    val email: String,
    val initialNickname: String
)

fun UserInitData.asModel() = UserInitModel(
    uid = uid,
    email = email,
    initialNickname = initialNickname
)

fun UserInitModel.asData() = UserInitData(
    uid = uid,
    email = email,
    initialNickname = initialNickname
)