package com.fancymansion.data.datasource.firebase.storage.book

import android.net.Uri
import java.io.File

interface BookFirebaseStorage {

    suspend fun uploadEpisodeZipFile(
        publishedId: String, version: Int, uri: Uri
    )
    suspend fun deleteBookStorageByPublishedId(publishedId: String)
    suspend fun pruneEpisodeZipFile(publishedId: String, currentVersion: Int, keep: Int)
    suspend fun uploadBookCoverImage(
        publishedId: String, coverFile: File, fileName: String
    )

    suspend fun downloadBookZipToCache(publishedId: String, version: Int, cacheFile: File): File
    suspend fun getBookCoverImageUrl(publishedId: String, imageFileName: String): String
}