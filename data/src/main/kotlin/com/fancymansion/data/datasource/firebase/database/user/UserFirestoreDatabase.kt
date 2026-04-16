package com.fancymansion.data.datasource.firebase.database.user

import com.fancymansion.data.datasource.firebase.auth.model.UserInitData
import com.fancymansion.data.datasource.firebase.database.user.model.BookRefData
import com.fancymansion.data.datasource.firebase.database.user.model.UserStoreData

interface UserFirestoreDatabase {
    suspend fun getOrCreateUserInfoTx(userInit: UserInitData): UserStoreData
    suspend fun updateNickname(uid: String, newNickname: String)
    suspend fun addPublishedBookRef(userId: String, bookRef: BookRefData)
    suspend fun removePublishedBookRef(userId: String, bookRef: BookRefData)
    suspend fun updatePublishedBookRef(userId: String, oldRef: BookRefData, newRef: BookRefData)
}