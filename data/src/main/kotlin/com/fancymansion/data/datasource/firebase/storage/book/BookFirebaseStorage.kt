package com.fancymansion.data.datasource.firebase.storage.book

import java.io.File

interface BookFirebaseStorage {
    suspend fun uploadBookCoverImage(
        publishedId: String, coverFile: File, fileName: String
    )
}