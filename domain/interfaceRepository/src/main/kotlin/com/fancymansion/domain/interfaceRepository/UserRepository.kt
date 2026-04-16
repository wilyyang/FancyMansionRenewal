package com.fancymansion.domain.interfaceRepository

import com.fancymansion.domain.model.user.BookRefModel
import com.fancymansion.domain.model.user.UserInitModel
import com.fancymansion.domain.model.user.UserInfoModel
import com.fancymansion.domain.model.user.UserStoreResult
import com.fancymansion.domain.model.user.result.NicknameUpdateResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signInWithGoogle(idToken: String): UserInitModel
    suspend fun getOrCreateUserInfoTx(userInit: UserInitModel): UserStoreResult
    suspend fun updateNickname(uid: String, newNickname: String): NicknameUpdateResult
    suspend fun addRemotePublishedBookRef(userId: String, bookRef: BookRefModel)
    suspend fun removeRemotePublishedBookRef(userId: String, bookRef: BookRefModel)
    suspend fun updateRemotePublishedBookRef(userId: String, oldRef: BookRefModel, newRef: BookRefModel)
    suspend fun upsertUserInfoLocal(userInfo: UserInfoModel)
    suspend fun getUserInfoLocal(): UserInfoModel?
    fun observeUserInfoLocal(): Flow<UserInfoModel?>
    suspend fun getLocalPublishedBookRefs(): Set<BookRefModel>
    suspend fun replaceLocalPublishedBookRefs(bookRefs: Set<BookRefModel>)
    suspend fun addLocalPublishedBookRef(bookRef: BookRefModel)
    suspend fun updateLocalPublishedBookRef(oldRef: BookRefModel, newRef: BookRefModel)
    suspend fun removeLocalPublishedBookRef(bookRef: BookRefModel)
    suspend fun clearLocalPublishedBookRefs()
}