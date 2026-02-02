package com.fancymansion.data.datasource.firebase.storage.book

import android.net.Uri
import java.io.File

interface BookFirebaseStorage {

    suspend fun uploadEpisodeZipFile(
        publishedId: String, uri: Uri
    )

    suspend fun uploadBookCoverImage(
        publishedId: String, coverFile: File, fileName: String
    )

    suspend fun downloadBookZipToCache(publishedId: String, cacheFile: File): File
}