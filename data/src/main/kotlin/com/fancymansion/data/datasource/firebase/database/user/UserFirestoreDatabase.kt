package com.fancymansion.data.datasource.firebase.database.user

import com.fancymansion.data.datasource.firebase.auth.model.UserInitData
import com.fancymansion.data.datasource.firebase.database.user.model.UserStoreData

interface UserFirestoreDatabase {
    suspend fun getOrCreateUserInfoTx(userInit: UserInitData): UserStoreData
    suspend fun addPublishedBookId(userId: String, bookId: String)
    suspend fun removePublishedBookId(userId: String, bookId: String)
}