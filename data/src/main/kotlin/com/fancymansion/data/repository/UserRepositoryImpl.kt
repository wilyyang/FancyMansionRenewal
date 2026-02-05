package com.fancymansion.data.repository

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
        return userFirestoreDatabase.getOrCreateUserInfoTx(userInit.asData()).asModel()
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

    override suspend fun getPublishedBookIds(): Set<String> {
        return userStoreSource.getPublishedBookIds()
    }

    override suspend fun replacePublishedBookIds(ids: Set<String>) {
        userStoreSource.replacePublishedBookIds(ids)
    }

    override suspend fun addPublishedBookId(bookId: String) {
        userStoreSource.addPublishedBookId(bookId)
    }

    override suspend fun removePublishedBookId(bookId: String) {
        userStoreSource.removePublishedBookId(bookId)
    }

    override suspend fun clearPublishedBookIds() {
        userStoreSource.clearPublishedBookIds()
    }
}