package com.fancymansion.data.datasource.firebase.database.user

import com.fancymansion.data.datasource.firebase.FirestoreCollections
import com.fancymansion.data.datasource.firebase.auth.model.UserInitData
import com.fancymansion.data.datasource.firebase.database.user.model.UserInfoData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserFirestoreDatabaseImpl(
    private val firestore: FirebaseFirestore
) : UserFirestoreDatabase {

    override suspend fun getOrCreateUserInfoTx(userInit: UserInitData): UserInfoData {
        val ref = firestore
            .collection(FirestoreCollections.USERS)
            .document(userInit.uid)

        return firestore.runTransaction { transaction ->
            val snapshot = transaction.get(ref)
            val now = System.currentTimeMillis()

            if (snapshot.exists()) {
                val currentEmail = snapshot.getString(UserInfoData.EMAIL)
                val currentNickname = snapshot.getString(UserInfoData.NICKNAME)
                val currentCreatedAt = snapshot.getLong(UserInfoData.CREATED_AT)
                val currentUpdatedAt = snapshot.getLong(UserInfoData.UPDATED_AT)
                val currentOnboarding = snapshot.getBoolean(UserInfoData.HAS_COMPLETED_ONBOARDING)

                val updates = mutableMapOf<String, Any>()

                val willUpdateEmail = currentEmail == null || currentEmail != userInit.email
                if (willUpdateEmail) {
                    updates[UserInfoData.EMAIL] = userInit.email
                }

                val willUpdateNickname = currentNickname == null
                if (willUpdateNickname) {
                    updates[UserInfoData.NICKNAME] = userInit.initialNickname
                }

                val willUpdateOnboarding = currentOnboarding == null || !currentOnboarding
                if (willUpdateOnboarding) {
                    updates[UserInfoData.HAS_COMPLETED_ONBOARDING] = true
                }

                val didUpdate = updates.isNotEmpty()
                if (didUpdate) {
                    updates[UserInfoData.UPDATED_AT] = now
                    transaction.update(ref, updates)
                }

                val createdAt = currentCreatedAt ?: error("UserInfoData.CREATED_AT is null")
                val updatedAt = if (didUpdate) now else (currentUpdatedAt ?: createdAt)

                UserInfoData(
                    userId = userInit.uid,
                    email = if (willUpdateEmail) userInit.email else (currentEmail ?: error("UserInfoData.EMAIL is null")),
                    nickname = if (willUpdateNickname) userInit.initialNickname else (currentNickname ?: error("UserInfoData.NICKNAME is null")),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    hasCompletedOnboarding = true
                )
            } else {
                val data = UserInfoData(
                    userId = userInit.uid,
                    email = userInit.email,
                    nickname = userInit.initialNickname,
                    createdAt = now,
                    updatedAt = now,
                    hasCompletedOnboarding = false
                )
                transaction.set(ref, data)
                data
            }
        }.await()
    }
}