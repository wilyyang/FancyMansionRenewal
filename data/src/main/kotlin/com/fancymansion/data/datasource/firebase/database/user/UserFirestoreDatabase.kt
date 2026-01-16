package com.fancymansion.data.datasource.firebase.database.user

import com.fancymansion.data.datasource.firebase.auth.model.UserInitData
import com.fancymansion.data.datasource.firebase.database.user.model.UserInfoData

interface UserFirestoreDatabase {
    suspend fun getOrCreateUserInfoTx(userInit: UserInitData): UserInfoData
}