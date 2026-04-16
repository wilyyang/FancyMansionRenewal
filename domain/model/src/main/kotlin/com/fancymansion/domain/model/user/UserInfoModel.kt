package com.fancymansion.domain.model.user

data class UserStoreResult(
    val userInfo: UserInfoModel,
    val publishedBookRefs: Set<BookRefModel>,
    val collectedBookRefs: Set<BookRefModel>
)
data class UserInfoModel(
    val userId: String,
    val email: String,
    val nickname: String,
    val createdAt: Long,
    val updatedAt: Long,
    val hasCompletedOnboarding: Boolean
)

data class BookRefModel(
    val bookId: String,
    val version: Int
)