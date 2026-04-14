package com.fancymansion.domain.interfaceRepository

import com.fancymansion.domain.model.user.UserInitModel
import com.fancymansion.domain.model.user.UserInfoModel
import com.fancymansion.domain.model.user.UserStoreResult
import com.fancymansion.domain.model.user.result.NicknameUpdateResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signInWithGoogle(idToken: String): UserInitModel
    suspend fun getOrCreateUserInfoTx(userInit: UserInitModel): UserStoreResult
    suspend fun updateNickname(uid: String, newNickname: String): NicknameUpdateResult
    suspend fun addRemotePublishedBookId(userId: String, bookId: String)
    suspend fun removeRemotePublishedBookId(userId: String, bookId: String)
    suspend fun upsertUserInfoLocal(userInfo: UserInfoModel)
    suspend fun getUserInfoLocal(): UserInfoModel?
    fun observeUserInfoLocal(): Flow<UserInfoModel?>
    suspend fun getLocalPublishedBookIds(): Set<String>
    suspend fun replaceLocalPublishedBookIds(ids: Set<String>)
    suspend fun addLocalPublishedBookId(bookId: String)
    suspend fun removeLocalPublishedBookId(bookId: String)
    suspend fun clearLocalPublishedBookIds()
}