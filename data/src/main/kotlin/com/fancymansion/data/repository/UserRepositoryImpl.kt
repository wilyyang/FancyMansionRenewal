package com.fancymansion.data.repository

import com.fancymansion.data.common.NICKNAME_NOT_INIT
import com.fancymansion.data.common.NicknameDuplicateException
import com.fancymansion.data.common.generateNickname
import com.fancymansion.data.datasource.dataStore.user.UserStoreSource
import com.fancymansion.data.datasource.dataStore.user.model.asLocalData
import com.fancymansion.data.datasource.dataStore.user.model.asModel
import com.fancymansion.data.datasource.database.user.dao.UserDatabaseDao
import com.fancymansion.data.datasource.database.user.model.asLocalData
import com.fancymansion.data.datasource.database.user.model.asModel
import com.fancymansion.data.datasource.firebase.auth.UserAuthentication
import com.fancymansion.data.datasource.firebase.auth.model.asData
import com.fancymansion.data.datasource.firebase.auth.model.asModel
import com.fancymansion.data.datasource.firebase.database.user.UserFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.user.model.asData
import com.fancymansion.data.datasource.firebase.database.user.model.asModel
import com.fancymansion.domain.interfaceRepository.UserRepository
import com.fancymansion.domain.model.user.BookRefModel
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

    override suspend fun addRemotePublishedBookRef(userId: String, bookRef: BookRefModel) {
        userFirestoreDatabase.addPublishedBookRef(userId, bookRef.asData())
    }

    override suspend fun removeRemotePublishedBookRef(userId: String, bookRef: BookRefModel) {
        userFirestoreDatabase.removePublishedBookRef(userId, bookRef.asData())
    }

    override suspend fun updateRemotePublishedBookRef(
        userId: String,
        oldRef: BookRefModel,
        newRef: BookRefModel
    ) {
        userFirestoreDatabase.updatePublishedBookRef(userId, oldRef.asData(), newRef.asData())
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

    override suspend fun getLocalPublishedBookRefs(): Set<BookRefModel> {
        return userStoreSource.getPublishedBookRefs().map { it.asModel() }.toSet()
    }

    override suspend fun replaceLocalPublishedBookRefs(bookRefs: Set<BookRefModel>) {
        userStoreSource.replacePublishedBookRefs(bookRefs.map { it.asLocalData() }.toSet())
    }

    override suspend fun addLocalPublishedBookRef(bookRef: BookRefModel) {
        userStoreSource.addPublishedBookRef(bookRef.asLocalData())
    }

    override suspend fun updateLocalPublishedBookRef(
        oldRef: BookRefModel,
        newRef: BookRefModel
    ) {
        userStoreSource.updatePublishedBookRef(oldRef.asLocalData(), newRef.asLocalData())
    }

    override suspend fun removeLocalPublishedBookRef(bookRef: BookRefModel) {
        userStoreSource.removePublishedBookRef(bookRef.asLocalData())
    }

    override suspend fun clearLocalPublishedBookRefs() {
        userStoreSource.clearPublishedBookRefs()
    }
}