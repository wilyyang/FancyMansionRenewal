package com.fancymansion.data.datasource.database.user.model

import androidx.room.Entity
import com.fancymansion.domain.model.user.UserInfoModel

@Entity(
    tableName = "UserInfoLocalData",
    primaryKeys = ["userId"]
)
data class UserInfoLocalData(
    val userId: String,
    val email: String,
    val nickname: String,
    val createdAt: Long,
    val updatedAt: Long,
    val hasCompletedOnboarding: Boolean
)

fun UserInfoLocalData.asModel() = UserInfoModel(
    userId = userId,
    email = email,
    nickname = nickname,
    createdAt = createdAt,
    updatedAt = updatedAt,
    hasCompletedOnboarding = hasCompletedOnboarding
)

fun UserInfoModel.asLocalData() = UserInfoLocalData(
    userId = userId,
    email = email,
    nickname = nickname,
    createdAt = createdAt,
    updatedAt = updatedAt,
    hasCompletedOnboarding = hasCompletedOnboarding
)