package com.fancymansion.domain.interfaceRepository

import com.fancymansion.domain.model.user.UserInitModel
import com.fancymansion.domain.model.user.UserInfoModel

interface UserRepository {
    suspend fun signInWithGoogle(idToken: String): UserInitModel
    suspend fun getOrCreateUserInfoTx(userInit: UserInitModel): UserInfoModel
    suspend fun upsertUserInfoLocal(userInfo: UserInfoModel)
}