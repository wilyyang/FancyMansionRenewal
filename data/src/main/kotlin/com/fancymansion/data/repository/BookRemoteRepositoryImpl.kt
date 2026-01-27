package com.fancymansion.data.repository

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.firebase.database.book.BookFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.book.model.asData
import com.fancymansion.data.datasource.firebase.database.book.model.asModel
import com.fancymansion.data.datasource.firebase.storage.book.BookFirebaseStorage
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.HomeBookItemModel
import javax.inject.Inject

class BookRemoteRepositoryImpl @Inject constructor(
    private val bookStorageSource: BookStorageSource,
    private val bookFirestoreDatabase: BookFirestoreDatabase,
    private val bookFirebaseStorage: BookFirebaseStorage
) : BookRemoteRepository {

    override suspend fun createBookInfo(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel,
        episodeInfo: EpisodeInfoModel
    ): String {
        val publishedId = bookFirestoreDatabase.saveBook(bookInfo.asData())
        bookFirestoreDatabase.saveEpisode(publishedId, episodeInfo.asData())
        return publishedId
    }

    override suspend fun uploadBookArchive(episodeRef: EpisodeRef, publishedId: String) {
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
        episodeRef: EpisodeRef,
        publishedId: String,
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
        return bookFirestoreDatabase.loadBookList().map { it.asModel() }
    }
}