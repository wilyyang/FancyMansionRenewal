package com.fancymansion.data.datasource.firebase.database.user.model

import com.fancymansion.domain.model.user.UserInfoModel

data class UserInfoData(
    val userId: String,
    val email: String,
    val nickname: String,
    val createdAt: Long,
    val updatedAt: Long,
    val hasCompletedOnboarding: Boolean
) {
    companion object Fields {
        const val EMAIL = "email"
        const val NICKNAME = "nickname"
        const val CREATED_AT = "createdAt"
        const val UPDATED_AT = "updatedAt"
        const val HAS_COMPLETED_ONBOARDING = "hasCompletedOnboarding"
    }
}

fun UserInfoData.asModel() = UserInfoModel(
    userId = userId,
    email = email,
    nickname = nickname,
    createdAt = createdAt,
    updatedAt = updatedAt,
    hasCompletedOnboarding = hasCompletedOnboarding
)