package com.fancymansion.data.datasource.firebase.database.user

import com.fancymansion.data.datasource.firebase.FirestoreCollections
import com.fancymansion.data.datasource.firebase.auth.model.UserInitData
import com.fancymansion.data.datasource.firebase.database.user.model.UserStoreData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserFirestoreDatabaseImpl(
    private val firestore: FirebaseFirestore
) : UserFirestoreDatabase {

    override suspend fun getOrCreateUserInfoTx(userInit: UserInitData): UserStoreData {
        val ref = firestore
            .collection(FirestoreCollections.USERS)
            .document(userInit.uid)

        return firestore.runTransaction { transaction ->
            val snapshot = transaction.get(ref)
            val now = System.currentTimeMillis()

            if (snapshot.exists()) {
                val currentEmail = snapshot.getString(UserStoreData.EMAIL)
                val currentNickname = snapshot.getString(UserStoreData.NICKNAME)
                val currentCreatedAt = snapshot.getLong(UserStoreData.CREATED_AT)
                val currentUpdatedAt = snapshot.getLong(UserStoreData.UPDATED_AT)
                val currentOnboarding = snapshot.getBoolean(UserStoreData.HAS_COMPLETED_ONBOARDING)

                val updates = mutableMapOf<String, Any>()

                val willUpdateEmail = currentEmail == null || currentEmail != userInit.email
                if (willUpdateEmail) {
                    updates[UserStoreData.EMAIL] = userInit.email
                }

                val willUpdateNickname = currentNickname == null
                if (willUpdateNickname) {
                    updates[UserStoreData.NICKNAME] = userInit.initialNickname
                }

                val willUpdateOnboarding = currentOnboarding == null || !currentOnboarding
                if (willUpdateOnboarding) {
                    updates[UserStoreData.HAS_COMPLETED_ONBOARDING] = true
                }

                val didUpdate = updates.isNotEmpty()
                if (didUpdate) {
                    updates[UserStoreData.UPDATED_AT] = now
                    transaction.update(ref, updates)
                }

                val createdAt = currentCreatedAt ?: error("UserInfoData.CREATED_AT is null")
                val updatedAt = if (didUpdate) now else (currentUpdatedAt ?: createdAt)
                val publishedBookIds = (snapshot.get(UserStoreData.PUBLISHED_BOOK_IDS) as? List<*>)?.filterIsInstance<String>() ?: emptyList()

                UserStoreData(
                    userId = userInit.uid,
                    email = if (willUpdateEmail) userInit.email else currentEmail,
                    nickname = if (willUpdateNickname) userInit.initialNickname else currentNickname,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    hasCompletedOnboarding = true,
                    publishedBookIds = publishedBookIds
                )
            } else {
                val data = UserStoreData(
                    userId = userInit.uid,
                    email = userInit.email,
                    nickname = userInit.initialNickname,
                    createdAt = now,
                    updatedAt = now,
                    hasCompletedOnboarding = false,
                    publishedBookIds = emptyList()
                )
                transaction.set(ref, data)
                data
            }
        }.await()
    }

    override suspend fun addPublishedBookId(
        userId: String,
        bookId: String
    ) {
        val ref = firestore.collection(FirestoreCollections.USERS).document(userId)

        ref.update(
            mapOf(
                UserStoreData.PUBLISHED_BOOK_IDS to FieldValue.arrayUnion(bookId)
            )
        ).await()
    }

    override suspend fun removePublishedBookId(
        userId: String,
        bookId: String
    ) {
        val ref = firestore.collection(FirestoreCollections.USERS).document(userId)

        ref.update(
            mapOf(
                UserStoreData.PUBLISHED_BOOK_IDS to FieldValue.arrayRemove(bookId)
            )
        ).await()
    }
}