package com.fancymansion.data.datasource.firebase.database.book

import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.core.common.const.RemotePublishStatus
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.data.datasource.firebase.FirestoreCollections.BOOKS
import com.fancymansion.data.datasource.firebase.FirestoreCollections.EPISODES
import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData.Fields.QueryFields.TITLE_KEY
import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData.Fields.QueryFields.UPDATE_KEY
import com.fancymansion.data.datasource.firebase.database.book.model.EditorData
import com.fancymansion.data.datasource.firebase.database.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.HomeBookItemData
import com.fancymansion.data.datasource.firebase.database.book.model.IntroduceData
import com.fancymansion.data.datasource.firebase.database.book.model.NOT_ASSIGN_PUBLISHED_AT
import com.fancymansion.data.datasource.firebase.database.book.model.NOT_ASSIGN_PUBLISHED_ID
import com.fancymansion.data.datasource.firebase.database.book.model.NOT_ASSIGN_UPDATED_AT
import com.fancymansion.data.datasource.firebase.database.book.model.PublishInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.result.BookQueryData
import com.fancymansion.data.datasource.firebase.database.book.model.result.BookQueryDataResult
import com.fancymansion.data.datasource.firebase.database.book.model.result.LoadBookDataResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
            BookInfoData.PUBLISH_INFO to mapOf(
                PublishInfoData.PUBLISHED_ID to book.publishInfo.publishedId,
                PublishInfoData.PUBLISHED_AT to book.publishInfo.publishedAt,
                PublishInfoData.UPDATED_AT to book.publishInfo.updatedAt,
                PublishInfoData.VERSION to book.publishInfo.version,
                PublishInfoData.PUBLISH_STATUS to book.publishInfo.publishStatus.name
            ),
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

    override suspend fun updateBook(publishedId: String, book: BookInfoData) {
        val ref = firestore.collection(BOOKS).document(publishedId)
        val currentTime = System.currentTimeMillis()

        val updates = hashMapOf(
            BookInfoData.BOOK_ID to book.bookId,

            "${BookInfoData.PUBLISH_INFO}.${PublishInfoData.UPDATED_AT}" to book.publishInfo.updatedAt,
            "${BookInfoData.PUBLISH_INFO}.${PublishInfoData.VERSION}" to book.publishInfo.version,

            "${BookInfoData.INTRODUCE}.${IntroduceData.TITLE}" to book.introduce.title,
            "${BookInfoData.INTRODUCE}.${IntroduceData.COVER_LIST}" to book.introduce.coverList,
            "${BookInfoData.INTRODUCE}.${IntroduceData.KEYWORD_IDS}" to book.introduce.keywordIds,
            "${BookInfoData.INTRODUCE}.${IntroduceData.DESCRIPTION}" to book.introduce.description,

            "${BookInfoData.EDITOR}.${EditorData.EDITOR_ID}" to book.editor.editorId,
            "${BookInfoData.EDITOR}.${EditorData.EDITOR_NAME}" to book.editor.editorName,
            "${BookInfoData.EDITOR}.${EditorData.EDITOR_EMAIL}" to book.editor.editorEmail,
        )

        ref.update(updates).await()
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

            EpisodeInfoData.CREATE_TIME to episode.createTime,
            EpisodeInfoData.EDIT_TIME to episode.editTime
        )

        ref.set(data, SetOptions.merge()).await()
    }

    override suspend fun updateEpisode(
        publishedId: String,
        episode: EpisodeInfoData
    ) {
        val ref = firestore.collection(BOOKS)
            .document(publishedId)
            .collection(EPISODES)
            .document(episode.episodeId)

        val updates = hashMapOf<String, Any>(
            EpisodeInfoData.EPISODE_ID to episode.episodeId,
            EpisodeInfoData.BOOK_ID to episode.bookId,
            EpisodeInfoData.TITLE to episode.title,
            EpisodeInfoData.PAGE_COUNT to episode.pageCount,

            EpisodeInfoData.CREATE_TIME to episode.createTime,
            EpisodeInfoData.EDIT_TIME to episode.editTime
        )

        ref.update(updates).await()
    }

    override suspend fun loadBookListWithQuery(
        searchText: String,
        sortOrder: RemoteBookSortOrder,
        cursorBookId: String?,
        limit: Long
    ): BookQueryDataResult {

        // 배포 상태
        val query = firestore.collection(BOOKS).whereEqualTo(
            "${BookInfoData.PUBLISH_INFO}.${PublishInfoData.PUBLISH_STATUS}",
            RemotePublishStatus.PUBLISHED.name
        ).let { base ->
            // 검색
            if (searchText.isNotBlank()) {
                // 검색은 제목 정렬에서만 기능
                if(sortOrder != RemoteBookSortOrder.TITLE_ASCENDING)
                    return BookQueryDataResult.InvalidSearch

                base.whereGreaterThanOrEqualTo(TITLE_KEY, searchText)
                    .whereLessThanOrEqualTo(TITLE_KEY, searchText + "\uf8ff")
            } else base
        }.let { searched ->
            // 정렬
            when (sortOrder) {
                RemoteBookSortOrder.TITLE_ASCENDING -> searched.orderBy(TITLE_KEY)
                RemoteBookSortOrder.LAST_UPDATE -> searched.orderBy(
                    UPDATE_KEY,
                    Query.Direction.DESCENDING
                )
            }
        }.let { sorted ->
            // 커서
            if (cursorBookId != null) {
                val startSnapshot = firestore.collection(BOOKS).document(cursorBookId).get().await()

                if (!startSnapshot.exists())
                    return BookQueryDataResult.CursorNotExist

                sorted.startAt(startSnapshot)
            } else sorted
        }.limit(limit + 1) // next id 를 가져오기 위함

        // 변환 및 반환
        val bookSnapshots = query.get().await()
        return coroutineScope {
            val results = bookSnapshots.documents.map { bookDoc ->
                async {
                    val bookId = bookDoc.getString(BookInfoData.BOOK_ID) ?: return@async null
                    val book = BookInfoData(
                        bookId = bookId,
                        publishInfo = bookDoc.parsePublishInfo(),
                        introduce = bookDoc.parseIntroduce(),
                        editor = bookDoc.parseEditor(),
                    )
                    val episode = getEpisodeData(bookId) ?: return@async null
                    HomeBookItemData(book = book, episode = episode)
                }
            }.awaitAll()

            if (results.any { it == null }) {
                BookQueryDataResult.NotFoundBook
            } else {
                val safeResults = results.filterNotNull()
                val (bookList, nextBookId) = safeResults.let { list ->
                    if(list.size > limit){
                        val nextId = list.last().book.bookId
                        Pair(list.dropLast(1), nextId)
                    }else{
                        Pair(list, null)
                    }
                }

                BookQueryDataResult.Success(
                    BookQueryData(
                        bookList = bookList,
                        nextBookId = nextBookId
                    )
                )
            }
        }
    }

    override suspend fun loadBookList(): List<HomeBookItemData> {
        val bookSnapshots = firestore.collection(BOOKS)
            .whereEqualTo(
                "${BookInfoData.PUBLISH_INFO}.${PublishInfoData.PUBLISH_STATUS}",
                RemotePublishStatus.PUBLISHED.name
            )
            .get().await()
        if (bookSnapshots.isEmpty) return emptyList()

        return coroutineScope {
            bookSnapshots.documents.map { bookDoc ->
                async {
                    val bookId = bookDoc.getString(BookInfoData.BOOK_ID) ?: return@async null
                    val book = BookInfoData(
                        bookId = bookId,
                        publishInfo = bookDoc.parsePublishInfo(),
                        introduce = bookDoc.parseIntroduce(),
                        editor = bookDoc.parseEditor(),
                    )

                    val episode = getEpisodeData(bookId) ?: return@async null
                    HomeBookItemData(book = book, episode = episode)
                }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun loadSelectedBookList(
        bookIds: List<String>
    ): List<HomeBookItemData> {

        if (bookIds.isEmpty()) return emptyList()

        return coroutineScope {
            bookIds.map { bookId ->
                async {
                    try {
                        val bookDoc = firestore
                            .collection(BOOKS)
                            .document(bookId)
                            .get()
                            .await()

                        if (!bookDoc.exists()) return@async null

                        val book = BookInfoData(
                            bookId = bookId,
                            publishInfo = bookDoc.parsePublishInfo(),
                            introduce = bookDoc.parseIntroduce(),
                            editor = bookDoc.parseEditor(),
                        )

                        // 철회된 북는 가져올 수 없음
                        if (book.publishInfo.publishStatus != RemotePublishStatus.PUBLISHED)
                            return@async null

                        val episode = getEpisodeData(bookId) ?: return@async null
                        HomeBookItemData(book = book, episode = episode)
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun loadSelectedBook(bookId: String): LoadBookDataResult {
        val bookDoc = firestore
            .collection(BOOKS)
            .document(bookId)
            .get()
            .await()

        if(!bookDoc.exists()){
            return LoadBookDataResult.NotFound
        }

        val book = BookInfoData(
            bookId = bookId,
            publishInfo = bookDoc.parsePublishInfo(),
            introduce = bookDoc.parseIntroduce(),
            editor = bookDoc.parseEditor(),
        )

        if(book.publishInfo.publishStatus != RemotePublishStatus.PUBLISHED){
            return LoadBookDataResult.Withdrawn
        }

        val episode = getEpisodeData(bookId) ?: error("Episode not found for bookId=$bookId")

        return LoadBookDataResult.Success(HomeBookItemData(book = book, episode = episode))
    }

    override suspend fun getPublishedBookVersion(publishedId: String): Int {
        val snapshot = firestore
            .collection(BOOKS)
            .document(publishedId)
            .get()
            .await()

        val publishInfo = snapshot.get(BookInfoData.PUBLISH_INFO) as? Map<*, *>
            ?: error("PublishInfo not found")

        return (publishInfo[PublishInfoData.VERSION] as? Number)
            ?.toInt()
            ?: error("Version not found")
    }

    override suspend fun withdrawBookWithEpisodes(publishedId: String) {
        val ref = firestore.collection(BOOKS).document(publishedId)
        val currentTime = System.currentTimeMillis()

        val updates: Map<String, Any> = mapOf(
            "${BookInfoData.PUBLISH_INFO}.${PublishInfoData.UPDATED_AT}" to currentTime,
            "${BookInfoData.PUBLISH_INFO}.${PublishInfoData.PUBLISH_STATUS}" to RemotePublishStatus.WITHDRAWN.name
        )
        ref.update(updates).await()
    }

    private fun DocumentSnapshot.parsePublishInfo(): PublishInfoData {
        val publishInfoMap = get(BookInfoData.PUBLISH_INFO) as? Map<*, *>

        return PublishInfoData(
            publishedId = publishInfoMap?.get(PublishInfoData.PUBLISHED_ID) as? String ?: NOT_ASSIGN_PUBLISHED_ID,
            publishedAt = publishInfoMap?.get(PublishInfoData.PUBLISHED_AT) as? Long ?: NOT_ASSIGN_PUBLISHED_AT,
            updatedAt = publishInfoMap?.get(PublishInfoData.UPDATED_AT) as? Long ?: NOT_ASSIGN_UPDATED_AT,
            version = (publishInfoMap?.get(PublishInfoData.VERSION) as? Long ?: 0).toInt(),
            publishStatus = RemotePublishStatus.from(publishInfoMap?.get(PublishInfoData.PUBLISH_STATUS) as? String)
        )
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

            createTime = getLong(EpisodeInfoData.CREATE_TIME) ?: 0L,
            editTime = getLong(EpisodeInfoData.EDIT_TIME) ?: 0L
        )
    }

    private suspend fun getEpisodeData(
        bookId: String
    ): EpisodeInfoData? {
        val episodeId = getEpisodeId(bookId)
        val episodeDoc = firestore.collection(BOOKS)
            .document(bookId)
            .collection(EPISODES)
            .document(episodeId)
            .get()
            .await()

        if (!episodeDoc.exists()) return null

        return episodeDoc.parseEpisode(
            fallbackEpisodeId = episodeId,
            fallbackBookId = bookId
        )
    }
}