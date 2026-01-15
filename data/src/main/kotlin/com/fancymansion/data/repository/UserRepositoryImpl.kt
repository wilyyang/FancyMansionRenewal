package com.fancymansion.data.repository

import com.fancymansion.data.datasource.firebase.auth.UserAuthentication
import com.fancymansion.data.datasource.firebase.auth.model.asModel
import com.fancymansion.domain.interfaceRepository.UserRepository
import com.fancymansion.domain.model.user.UserInfoModel
import com.fancymansion.domain.model.user.UserInitModel
import javax.inject.Inject
import kotlin.String

class UserRepositoryImpl @Inject constructor(
    private val userAuthentication: UserAuthentication
) : UserRepository {

    override suspend fun signInWithGoogle(idToken: String): UserInitModel {
        return userAuthentication.signInWithGoogle(idToken).asModel()
    }

    override suspend fun getOrCreateUserInfoTx(userInit: UserInitModel): UserInfoModel {
        // TODO
        return UserInfoModel(
            uid = userInit.uid,
            email = userInit.email,
            nickname = userInit.initialNickname,
            createdAt = 0L,
            updatedAt = 0L,
            hasCompletedOnboarding = false
        )
    }

    override suspend fun upsertUserInfoLocal(userInfo: UserInfoModel) {
        // TODO
    }
}