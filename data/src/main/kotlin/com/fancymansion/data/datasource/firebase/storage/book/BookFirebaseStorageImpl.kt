package com.fancymansion.data.datasource.firebase.storage.book

import com.fancymansion.data.datasource.firebase.StorageCollections.BOOKS
import com.fancymansion.data.datasource.firebase.StorageCollections.COVERS
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

class BookFirebaseStorageImpl(
    private val storage: FirebaseStorage
) : BookFirebaseStorage {
    override suspend fun uploadBookCoverImage(
        publishedId: String,
        coverFile: File,
        fileName: String
    ) {
        require(publishedId.isNotBlank()) { "publishedId is blank" }
        require(coverFile.exists()) { "coverFile does not exist: ${coverFile.path}" }
        require(fileName.isNotBlank()) { "fileName is blank" }

        val storagePath = "${BOOKS}/$publishedId/${COVERS}/$fileName"
        val ref = storage.reference.child(storagePath)
        val bytes = coverFile.readBytes()
        ref.putBytes(bytes).await()
    }
}