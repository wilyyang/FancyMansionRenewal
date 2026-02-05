package com.fancymansion.domain.model.user

data class UserStoreResult(
    val userInfo: UserInfoModel,
    val publishedBookIds: Set<String>
)
data class UserInfoModel(
    val userId: String,
    val email: String,
    val nickname: String,
    val createdAt: Long,
    val updatedAt: Long,
    val hasCompletedOnboarding: Boolean
)