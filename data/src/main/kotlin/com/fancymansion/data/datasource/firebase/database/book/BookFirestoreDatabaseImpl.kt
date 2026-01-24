package com.fancymansion.data.datasource.firebase.database.book

import com.fancymansion.data.datasource.firebase.FirestoreCollections.BOOKS
import com.fancymansion.data.datasource.firebase.FirestoreCollections.EPISODES
import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.EditorData
import com.fancymansion.data.datasource.firebase.database.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.IntroduceData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class BookFirestoreDatabaseImpl(
    private val firestore: FirebaseFirestore
) : BookFirestoreDatabase {

    override suspend fun saveBook(book: BookInfoData): String {
        val publishedId = firestore.collection(BOOKS).document().id
        val ref = firestore.collection(BOOKS).document(publishedId)

        val data = hashMapOf(
            BookInfoData.BOOK_ID to book.bookId,
            BookInfoData.PUBLISHED_ID to publishedId,
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
        return publishedId
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
            EpisodeInfoData.EDIT_TIME to episode.editTime,
            EpisodeInfoData.UPDATE_TIME to episode.updateTime,
        )

        ref.set(data, SetOptions.merge()).await()
    }
}