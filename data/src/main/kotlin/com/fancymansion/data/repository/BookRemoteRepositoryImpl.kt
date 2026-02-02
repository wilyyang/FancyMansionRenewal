package com.fancymansion.data.repository

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.firebase.database.book.BookFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.book.model.asData
import com.fancymansion.data.datasource.firebase.database.book.model.asHomeModel
import com.fancymansion.data.datasource.firebase.storage.book.BookFirebaseStorage
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
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
        bookFirestoreDatabase.saveBook(publishedId, bookInfo.asData())
        bookFirestoreDatabase.saveEpisode(publishedId, episodeInfo.asData())
    }

    override suspend fun uploadBookArchive(publishedId: String, episodeRef: EpisodeRef) {
        val zipFile = bookStorageSource.compressEpisodeDirAndGetFile(episodeRef, publishedId)

        try {
            bookFirebaseStorage.uploadEpisodeZipFile(
                publishedId,
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

    override suspend fun getHomeBookItems(): List<HomeBookItemModel> {
        return bookFirestoreDatabase.loadBookList().map { it.asHomeModel() }
    }

    override suspend fun downloadBookArchive(userId: String, publishedId: String): File {
        val zipFile = bookStorageSource.getCacheZipFile(userId, publishedId)
        val downloadedZip = bookFirebaseStorage.downloadBookZipToCache(publishedId, zipFile)
        val extractedDir = bookStorageSource.unzipBookArchiveToUserDir(downloadedZip, userId, ReadMode.READ, publishedId)
        downloadedZip.delete()
        return extractedDir
    }

    override suspend fun getBookCoverImageUrl(
        publishedId: String,
        imageFileName: String
    ): String {
        return bookFirebaseStorage.getBookCoverImageUrl(publishedId, imageFileName)
    }
}