package com.fancymansion.data.datasource.firebase.database.book

import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.data.datasource.firebase.FirestoreCollections.BOOKS
import com.fancymansion.data.datasource.firebase.FirestoreCollections.EPISODES
import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.EditorData
import com.fancymansion.data.datasource.firebase.database.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.HomeBookItemData
import com.fancymansion.data.datasource.firebase.database.book.model.IntroduceData
import com.fancymansion.data.datasource.firebase.database.book.model.NOT_ASSIGN_PUBLISHED_AT
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class BookFirestoreDatabaseImpl(
    private val firestore: FirebaseFirestore
) : BookFirestoreDatabase {

    override suspend fun getPublishedId(): String {
        return firestore.collection(BOOKS).document().id
    }

    override suspend fun saveBook(publishedId: String, book: BookInfoData) {
        val ref = firestore.collection(BOOKS).document(publishedId)

        val data = hashMapOf(
            BookInfoData.BOOK_ID to book.bookId,
            BookInfoData.PUBLISHED_ID to publishedId,
            BookInfoData.PUBLISHED_AT to System.currentTimeMillis(),
            BookInfoData.INTRODUCE to mapOf(
                IntroduceData.TITLE to book.introduce.title,
                IntroduceData.COVER_LIST to book.introduce.coverList,
                IntroduceData.KEYWORD_IDS to book.introduce.keywordIds,
                IntroduceData.DESCRIPTION to book.introduce.description,
            ),
            BookInfoData.EDITOR to mapOf(
                EditorData.EDITOR_ID to book.editor.editorId,
                EditorData.EDITOR_NAME to book.editor.editorName,
                EditorData.EDITOR_EMAIL to book.editor.editorEmail,
            )
        )

        ref.set(data, SetOptions.merge()).await()
    }

    override suspend fun saveEpisode(publishedId: String, episode: EpisodeInfoData) {
        val ref = firestore.collection(BOOKS)
            .document(publishedId)
            .collection(EPISODES)
            .document(episode.episodeId)

        val data = hashMapOf(
            EpisodeInfoData.EPISODE_ID to episode.episodeId,
            EpisodeInfoData.BOOK_ID to episode.bookId,
            EpisodeInfoData.TITLE to episode.title,
            EpisodeInfoData.PAGE_COUNT to episode.pageCount,
            EpisodeInfoData.VERSION to episode.version,

            EpisodeInfoData.CREATE_TIME to episode.createTime,
            EpisodeInfoData.EDIT_TIME to episode.editTime
        )

        ref.set(data, SetOptions.merge()).await()
    }

    override suspend fun loadBookList(): List<HomeBookItemData> {
        val bookSnapshots = firestore.collection(BOOKS).get().await()
        if (bookSnapshots.isEmpty) return emptyList()

        return coroutineScope {
            bookSnapshots.documents.map { bookDoc ->
                async {
                    val publishedId = bookDoc.id
                    val bookId = bookDoc.getString(BookInfoData.BOOK_ID) ?: return@async null

                    val book = BookInfoData(
                        bookId = bookId,
                        publishedId = bookDoc.getString(BookInfoData.PUBLISHED_ID) ?: publishedId,
                        publishedAt = bookDoc.getLong(BookInfoData.PUBLISHED_AT) ?: NOT_ASSIGN_PUBLISHED_AT,
                        introduce = bookDoc.parseIntroduce(),
                        editor = bookDoc.parseEditor(),
                    )

                    val episodeId = getEpisodeId(bookId)
                    val episodeDoc = firestore.collection(BOOKS)
                        .document(publishedId)
                        .collection(EPISODES)
                        .document(episodeId)
                        .get()
                        .await()

                    if (!episodeDoc.exists()) return@async null

                    val episode = episodeDoc.parseEpisode(
                        fallbackEpisodeId = episodeId,
                        fallbackBookId = bookId
                    )

                    HomeBookItemData(book = book, episode = episode)
                }
            }.awaitAll().filterNotNull()
        }
    }

    private fun DocumentSnapshot.parseIntroduce(): IntroduceData {
        val introduceMap = get(BookInfoData.INTRODUCE) as? Map<*, *>

        return IntroduceData(
            title = introduceMap?.get(IntroduceData.TITLE) as? String ?: "",
            coverList = (introduceMap?.get(IntroduceData.COVER_LIST) as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            keywordIds = (introduceMap?.get(IntroduceData.KEYWORD_IDS) as? List<*>)?.filterIsInstance<Long>() ?: emptyList(),
            description = introduceMap?.get(IntroduceData.DESCRIPTION) as? String ?: "",
        )
    }

    private fun DocumentSnapshot.parseEditor(): EditorData {
        val editorMap = get(BookInfoData.EDITOR) as? Map<*, *>

        return EditorData(
            editorId = editorMap?.get(EditorData.EDITOR_ID) as? String ?: "",
            editorName = editorMap?.get(EditorData.EDITOR_NAME) as? String ?: "",
            editorEmail = editorMap?.get(EditorData.EDITOR_EMAIL) as? String ?: "",
        )
    }

    private fun DocumentSnapshot.parseEpisode(
        fallbackEpisodeId: String = id,
        fallbackBookId: String = ""
    ): EpisodeInfoData {
        return EpisodeInfoData(
            episodeId = getString(EpisodeInfoData.EPISODE_ID) ?: fallbackEpisodeId,
            bookId = getString(EpisodeInfoData.BOOK_ID) ?: fallbackBookId,
            title = getString(EpisodeInfoData.TITLE) ?: "",
            pageCount = (getLong(EpisodeInfoData.PAGE_COUNT) ?: 0L).toInt(),
            version = getLong(EpisodeInfoData.VERSION) ?: 0L,

            createTime = getLong(EpisodeInfoData.CREATE_TIME) ?: 0L,
            editTime = getLong(EpisodeInfoData.EDIT_TIME) ?: 0L
        )
    }
}