package com.fancymansion.data.repository

import com.fancymansion.data.common.NICKNAME_NOT_INIT
import com.fancymansion.data.common.NicknameDuplicateException
import com.fancymansion.data.common.generateNickname
import com.fancymansion.data.datasource.dataStore.user.UserStoreSource
import com.fancymansion.data.datasource.database.user.dao.UserDatabaseDao
import com.fancymansion.data.datasource.database.user.model.asLocalData
import com.fancymansion.data.datasource.database.user.model.asModel
import com.fancymansion.data.datasource.firebase.auth.UserAuthentication
import com.fancymansion.data.datasource.firebase.auth.model.asData
import com.fancymansion.data.datasource.firebase.auth.model.asModel
import com.fancymansion.data.datasource.firebase.database.user.UserFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.user.model.asModel
import com.fancymansion.domain.interfaceRepository.UserRepository
import com.fancymansion.domain.model.user.UserInfoModel
import com.fancymansion.domain.model.user.UserInitModel
import com.fancymansion.domain.model.user.UserStoreResult
import com.fancymansion.domain.model.user.result.NicknameUpdateResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.String

class UserRepositoryImpl @Inject constructor(
    private val userAuthentication: UserAuthentication,
    private val userFirestoreDatabase: UserFirestoreDatabase,
    private val userDatabaseDao: UserDatabaseDao,
    private val userStoreSource: UserStoreSource
) : UserRepository {

    override suspend fun signInWithGoogle(idToken: String): UserInitModel {
        return userAuthentication.signInWithGoogle(idToken).asModel()
    }

    override suspend fun getOrCreateUserInfoTx(userInit: UserInitModel): UserStoreResult {
        val userInfo = userFirestoreDatabase.getOrCreateUserInfoTx(userInit.asData())

        if (userInfo.nickname == NICKNAME_NOT_INIT) {
            var newNickname: String
            while (true) {
                try {
                    newNickname = generateNickname()
                    userFirestoreDatabase.updateNickname(userInit.uid, newNickname)
                    break
                } catch (e: NicknameDuplicateException) {}
            }

            return userInfo.copy(
                nickname = newNickname
            ).asModel()
        }

        return userInfo.asModel()
    }

    override suspend fun updateNickname(uid: String, newNickname: String): NicknameUpdateResult {
        return try {
            userFirestoreDatabase.updateNickname(uid, newNickname)
            NicknameUpdateResult.Success
        } catch (e: NicknameDuplicateException) {
            NicknameUpdateResult.Invalid.Duplicate
        }
    }

    override suspend fun addRemotePublishedBookId(userId: String, bookId: String) {
        userFirestoreDatabase.addPublishedBookId(userId, bookId)
    }

    override suspend fun removeRemotePublishedBookId(userId: String, bookId: String) {
        userFirestoreDatabase.removePublishedBookId(userId, bookId)
    }

    override suspend fun upsertUserInfoLocal(userInfo: UserInfoModel) {
        userDatabaseDao.replaceUserInfo(userInfo.asLocalData())
    }

    override suspend fun getUserInfoLocal(): UserInfoModel? {
        return userDatabaseDao.getUserInfoData()?.asModel()
    }

    override fun observeUserInfoLocal(): Flow<UserInfoModel?> {
        return userDatabaseDao.observeUserInfoData().map { it?.asModel() }
    }

    override suspend fun getLocalPublishedBookIds(): Set<String> {
        return userStoreSource.getPublishedBookIds()
    }

    override suspend fun replaceLocalPublishedBookIds(ids: Set<String>) {
        userStoreSource.replacePublishedBookIds(ids)
    }

    override suspend fun addLocalPublishedBookId(bookId: String) {
        userStoreSource.addPublishedBookId(bookId)
    }

    override suspend fun removeLocalPublishedBookId(bookId: String) {
        userStoreSource.removePublishedBookId(bookId)
    }

    override suspend fun clearLocalPublishedBookIds() {
        userStoreSource.clearPublishedBookIds()
    }
}