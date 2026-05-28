package com.fancymansion.data.repository

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.NEXT_CURSOR_COUNT
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.core.common.const.RemotePublishStatus
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.firebase.database.book.BookFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.book.model.NOT_ASSIGN_PUBLISHED_AT
import com.fancymansion.data.datasource.firebase.database.book.model.PublishInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.asData
import com.fancymansion.data.datasource.firebase.database.book.model.asHomeModel
import com.fancymansion.data.datasource.firebase.database.book.model.result.BookQueryDataResult
import com.fancymansion.data.datasource.firebase.database.book.model.result.NextBookIdDataResult
import com.fancymansion.data.datasource.firebase.database.book.model.result.toDomain
import com.fancymansion.data.datasource.firebase.storage.book.BookFirebaseStorage
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import com.fancymansion.domain.model.homeBook.result.BookQueryModel
import com.fancymansion.domain.model.homeBook.result.BookQueryResult
import com.fancymansion.domain.model.homeBook.result.LoadBookResult
import java.io.File
import javax.inject.Inject

class BookRemoteRepositoryImpl @Inject constructor(
    private val bookStorageSource: BookStorageSource,
    private val bookFirestoreDatabase: BookFirestoreDatabase,
    private val bookFirebaseStorage: BookFirebaseStorage
) : BookRemoteRepository {

    override suspend fun getPublishedId(): String {
        return bookFirestoreDatabase.getPublishedId()
    }

    override suspend fun createBookInfo(
        publishedId: String,
        bookInfo: BookInfoModel,
        episodeInfo: EpisodeInfoModel
    ) {
        val currentTime = System.currentTimeMillis()
        val publishInfo = PublishInfoData(
            publishedId = publishedId,
            publishedAt = currentTime,
            updatedAt = currentTime,
            version = 0,
            publishStatus = RemotePublishStatus.PUBLISHED
        )
        bookFirestoreDatabase.saveBook(publishedId, bookInfo.asData(publishInfo))
        bookFirestoreDatabase.saveEpisode(publishedId, episodeInfo.asData())
    }

    override suspend fun updateBookInfo(
        publishedId: String,
        bookInfo: BookInfoModel,
        episodeInfo: EpisodeInfoModel,
        version: Int
    ) {
        val currentTime = System.currentTimeMillis()
        val publishInfo = PublishInfoData(
            publishedId = publishedId,
            publishedAt = NOT_ASSIGN_PUBLISHED_AT,
            updatedAt = currentTime,
            version = version,
            publishStatus = RemotePublishStatus.PUBLISHED
        )
        bookFirestoreDatabase.updateBook(publishedId, bookInfo.asData(publishInfo))
        bookFirestoreDatabase.updateEpisode(publishedId, episodeInfo.asData())
    }

    override suspend fun uploadBookArchive(publishedId: String, version: Int, episodeRef: EpisodeRef) {
        val zipFile = bookStorageSource.compressEpisodeDirAndGetFile(episodeRef, publishedId)

        try {
            bookFirebaseStorage.uploadEpisodeZipFile(
                publishedId,
                version,
                Uri.fromFile(zipFile)
            )
        } finally {
            if (zipFile.exists()) {
                zipFile.delete()
            }
        }
    }

    override suspend fun uploadBookCoverImage(
        publishedId: String,
        episodeRef: EpisodeRef,
        coverFileName: String
    ) {
        val coverFile = bookStorageSource.loadCoverImage(
            userId = episodeRef.userId,
            mode = episodeRef.mode,
            bookId = episodeRef.bookId,
            imageName = coverFileName
        )

        bookFirebaseStorage.uploadBookCoverImage(
            publishedId = publishedId,
            coverFile = coverFile,
            fileName = coverFileName
        )
    }

    override suspend fun getHomeBookItemsWithQuery(
        searchText: String,
        sortOrder: RemoteBookSortOrder,
        cursorBookIds: List<String>,
        limit: Int
    ): BookQueryResult {

        var finalResult: BookQueryDataResult? = null

        // 1. 북 정보 불러오기
        val cursorCandidates = cursorBookIds.ifEmpty { listOf<String?>(null) }
        for (cursorBookId in cursorCandidates) {
            when (val result = bookFirestoreDatabase.loadBookListWithQuery(
                searchText = searchText,
                sortOrder = sortOrder,
                cursorBookId = cursorBookId,
                limit = limit
            )) {
                BookQueryDataResult.InvalidSearch -> return BookQueryResult.Error.InvalidSearch
                BookQueryDataResult.NotFoundBook -> return BookQueryResult.Error.NotFoundBook

                BookQueryDataResult.CursorNotExist -> continue
                is BookQueryDataResult.Success -> {
                    finalResult = result
                    break
                }
            }
        }

        val successResult =
            finalResult as? BookQueryDataResult.Success
                ?: return BookQueryResult.Error.CursorNotExist

        // 2. 다음 book id 목록
        val lastBookId = successResult.bookList.lastOrNull()?.book?.bookId
        val nextIds = if (lastBookId != null) {
            when (val nextResult = bookFirestoreDatabase.getNextBookIds(
                searchText = searchText,
                sortOrder = sortOrder,
                beforeBookId = lastBookId,
                limit = NEXT_CURSOR_COUNT
            )) {
                is NextBookIdDataResult.Success -> nextResult.nextBookIds
                else -> return BookQueryResult.Error.NextBookIdError
            }

        } else emptyList()

        return BookQueryResult.Success(
            model = BookQueryModel(
                bookList = successResult.bookList.map { it.asHomeModel() },
                nextBookIds = nextIds
            )
        )
    }

    override suspend fun getHomeBookItems(): List<HomeBookItemModel> {
        return bookFirestoreDatabase.loadBookList().map { it.asHomeModel() }
    }

    override suspend fun getSelectedHomeBookItems(bookIds: List<String>): List<HomeBookItemModel> {
        return bookFirestoreDatabase.loadSelectedBookList(bookIds).map { it.asHomeModel() }
    }

    override suspend fun getSelectedHomeBookItem(bookId: String): LoadBookResult {
        return bookFirestoreDatabase.loadSelectedBook(bookId).toDomain()
    }

    override suspend fun downloadBookArchive(userId: String, version: Int, publishedId: String, readMode: ReadMode): File {
        val zipFile = bookStorageSource.getCacheZipFile(userId, publishedId)
        val downloadedZip = bookFirebaseStorage.downloadBookZipToCache(publishedId, version, zipFile)
        val extractedDir = bookStorageSource.unzipBookArchiveToUserDir(downloadedZip, userId, readMode, publishedId)
        downloadedZip.delete()
        return extractedDir
    }

    override suspend fun getBookCoverImageUrl(
        publishedId: String,
        imageFileName: String
    ): String {
        return bookFirebaseStorage.getBookCoverImageUrl(publishedId, imageFileName)
    }

    override suspend fun getPublishedBookVersion(publishedId: String): Int {
        return bookFirestoreDatabase.getPublishedBookVersion(publishedId)
    }

    override suspend fun withdrawBookWithEpisodes(publishedId: String) {
        bookFirestoreDatabase.withdrawBookWithEpisodes(publishedId)
    }

    override suspend fun deleteBookStorageByPublishedId(publishedId: String) {
        bookFirebaseStorage.deleteBookStorageByPublishedId(publishedId)
    }

    override suspend fun pruneEpisodeZipFile(publishedId: String, currentVersion: Int) {
        bookFirebaseStorage.pruneEpisodeZipFile(publishedId, currentVersion, 3)
    }
}