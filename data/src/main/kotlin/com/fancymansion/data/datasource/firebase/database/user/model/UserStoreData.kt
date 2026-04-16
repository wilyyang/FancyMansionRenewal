package com.fancymansion.data.datasource.firebase.database.user.model

import com.fancymansion.domain.model.user.BookRefModel
import com.fancymansion.domain.model.user.UserInfoModel
import com.fancymansion.domain.model.user.UserStoreResult

data class BookRefData(
    val bookId: String,
    val version: Int
){
    companion object Fields {
        const val BOOK_ID = "bookId"
        const val VERSION = "version"
    }
}

data class UserStoreData(
    val userId: String,
    val email: String,
    val nickname: String,
    val createdAt: Long,
    val updatedAt: Long,
    val hasCompletedOnboarding: Boolean,
    val publishedBookRefs: List<BookRefData>,
    val collectedBookRefs: List<BookRefData>
) {
    companion object Fields {
        const val EMAIL = "email"
        const val NICKNAME = "nickname"
        const val CREATED_AT = "createdAt"
        const val UPDATED_AT = "updatedAt"
        const val HAS_COMPLETED_ONBOARDING = "hasCompletedOnboarding"
        const val PUBLISHED_BOOK_REFS = "publishedBookRefs"
        const val COLLECTED_BOOK_REFS = "collectedBookRefs"
    }
}

fun BookRefData.asModel() = BookRefModel(
    bookId = bookId,
    version = version
)

fun BookRefModel.asData() = BookRefData(
    bookId = bookId,
    version = version
)

fun UserStoreData.asModel() = UserStoreResult(
    userInfo = UserInfoModel(
        userId = userId,
        email = email,
        nickname = nickname,
        createdAt = createdAt,
        updatedAt = updatedAt,
        hasCompletedOnboarding = hasCompletedOnboarding
    ),
    publishedBookRefs = publishedBookRefs.map { it.asModel() }.toSet(),
    collectedBookRefs = collectedBookRefs.map { it.asModel() }.toSet()
)