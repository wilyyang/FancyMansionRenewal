package com.fancymansion.domain.interfaceRepository

import com.fancymansion.domain.model.user.UserInitModel
import com.fancymansion.domain.model.user.UserInfoModel
import com.fancymansion.domain.model.user.UserStoreResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signInWithGoogle(idToken: String): UserInitModel
    suspend fun getOrCreateUserInfoTx(userInit: UserInitModel): UserStoreResult
    suspend fun upsertUserInfoLocal(userInfo: UserInfoModel)
    suspend fun getUserInfoLocal(): UserInfoModel?
    fun observeUserInfoLocal(): Flow<UserInfoModel?>
    suspend fun getPublishedBookIds(): Set<String>
    suspend fun replacePublishedBookIds(ids: Set<String>)
    suspend fun addPublishedBookId(bookId: String)
    suspend fun removePublishedBookId(bookId: String)
    suspend fun clearPublishedBookIds()
}