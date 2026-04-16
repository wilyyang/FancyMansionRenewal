package com.fancymansion.data.datasource.firebase.database.user

import com.fancymansion.data.common.NICKNAME_NOT_INIT
import com.fancymansion.data.common.NicknameDuplicateException
import com.fancymansion.data.datasource.firebase.FirestoreCollections
import com.fancymansion.data.datasource.firebase.auth.model.UserInitData
import com.fancymansion.data.datasource.firebase.database.user.model.NicknameStoreData
import com.fancymansion.data.datasource.firebase.database.user.model.BookRefData
import com.fancymansion.data.datasource.firebase.database.user.model.UserStoreData
import com.google.firebase.firestore.DocumentSnapshot
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
                val nickname = snapshot.getString(UserStoreData.NICKNAME)
                    ?: error("User nickname not found")
                val createdAt = snapshot.getLong(UserStoreData.CREATED_AT)
                    ?: error("User create time not found")
                val currentUpdatedAt = snapshot.getLong(UserStoreData.UPDATED_AT)
                    ?: error("User update time not found")
                val currentOnboarding = snapshot.getBoolean(UserStoreData.HAS_COMPLETED_ONBOARDING)

                val updates = mutableMapOf<String, Any>()

                val willUpdateEmail = currentEmail == null || currentEmail != userInit.email
                if (willUpdateEmail) {
                    updates[UserStoreData.EMAIL] = userInit.email
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
                val updatedAt = if (didUpdate) now else currentUpdatedAt
                val publishedBookRefs = parseBookRefs(snapshot, UserStoreData.PUBLISHED_BOOK_REFS)
                val collectedBookRefs = parseBookRefs(snapshot, UserStoreData.COLLECTED_BOOK_REFS)

                UserStoreData(
                    userId = userInit.uid,
                    email = if (willUpdateEmail) userInit.email else currentEmail,
                    nickname = nickname,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    hasCompletedOnboarding = true,
                    publishedBookRefs = publishedBookRefs,
                    collectedBookRefs = collectedBookRefs
                )
            } else {
                val data = UserStoreData(
                    userId = userInit.uid,
                    email = userInit.email,
                    nickname = NICKNAME_NOT_INIT,
                    createdAt = now,
                    updatedAt = now,
                    hasCompletedOnboarding = false,
                    publishedBookRefs = emptyList(),
                    collectedBookRefs = emptyList()
                )
                transaction.set(ref, data)
                data
            }
        }.await()
    }

    override suspend fun updateNickname(
        uid: String,
        newNickname: String
    ) {
        firestore.runTransaction { transaction ->
            val userRef = firestore.collection(FirestoreCollections.USERS).document(uid)
            val newNickRef = firestore.collection(FirestoreCollections.NICKNAMES).document(newNickname)

            val userSnap = transaction.get(userRef).run {
                if (!exists()) error("User not found")
                this
            }

            val oldNickname = userSnap.getString(UserStoreData.NICKNAME)
                ?: error("User nickname not found")

            // 같은 닉네임이면 아무것도 안 함
            if (oldNickname == newNickname) return@runTransaction

            // 새 닉네임 중복 체크
            val newNickSnap = transaction.get(newNickRef)
            if (newNickSnap.exists()) {
                throw NicknameDuplicateException()
            }

            // 1. 기존 nickname 삭제 & 새 nickname 등록
            if (oldNickname != NICKNAME_NOT_INIT) {
                val oldNickRef = firestore.collection(FirestoreCollections.NICKNAMES).document(oldNickname)
                transaction.delete(oldNickRef)
            }
            transaction.set(newNickRef, mapOf(NicknameStoreData.UID to uid))

            // 2. user 업데이트
            val now = System.currentTimeMillis()
            transaction.update(
                userRef, mapOf(
                    UserStoreData.NICKNAME to newNickname,
                    UserStoreData.UPDATED_AT to now
                )
            )
        }.await()
    }

    override suspend fun addPublishedBookRef(
        userId: String,
        bookRef: BookRefData
    ) {
        val ref = firestore.collection(FirestoreCollections.USERS).document(userId)
        ref.update(
            mapOf(
                UserStoreData.PUBLISHED_BOOK_REFS to FieldValue.arrayUnion(bookRef.toMap())
            )
        ).await()
    }

    override suspend fun removePublishedBookRef(
        userId: String,
        bookRef: BookRefData
    ) {
        val ref = firestore.collection(FirestoreCollections.USERS).document(userId)

        ref.update(
            mapOf(
                UserStoreData.PUBLISHED_BOOK_REFS to FieldValue.arrayRemove(bookRef.toMap())
            )
        ).await()
    }

    override suspend fun updatePublishedBookRef(
        userId: String,
        oldRef: BookRefData,
        newRef: BookRefData
    ) {
        val ref = firestore.collection(FirestoreCollections.USERS).document(userId)

        firestore.runTransaction { transaction ->
            transaction.update(
                ref,
                UserStoreData.PUBLISHED_BOOK_REFS,
                FieldValue.arrayRemove(oldRef.toMap())
            )

            transaction.update(
                ref,
                UserStoreData.PUBLISHED_BOOK_REFS,
                FieldValue.arrayUnion(newRef.toMap())
            )
        }.await()
    }

    private fun parseBookRefs(snapshot: DocumentSnapshot, field: String): List<BookRefData> {
        return snapshot.get(field)
            ?.let { list ->
                (list as? List<Map<String, Any>>)?.mapNotNull { map ->
                    val bookId = map[BookRefData.BOOK_ID] as? String
                    val version = (map[BookRefData.VERSION] as? Long)?.toInt()

                    if (bookId != null && version != null) {
                        BookRefData(bookId, version)
                    } else null
                }
            } ?: emptyList()
    }

    private fun BookRefData.toMap(): Map<String, Any> {
        return mapOf(
            BookRefData.BOOK_ID to bookId,
            BookRefData.VERSION to version
        )
    }
}