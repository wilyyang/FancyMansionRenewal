package com.fancymansion.data.datasource.firebase.storage.book

import android.net.Uri
import com.fancymansion.data.datasource.firebase.StorageCollections.BOOKS
import com.fancymansion.data.datasource.firebase.StorageCollections.COVERS
import com.fancymansion.data.datasource.firebase.StorageCollections.EPISODES
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BookFirebaseStorageImpl(
    private val storage: FirebaseStorage
) : BookFirebaseStorage {

    private val zipFileName = "book.zip"
    override suspend fun uploadEpisodeZipFile(publishedId: String, uri: Uri) {
        require(publishedId.isNotBlank()) { "publishedId is blank" }
        require(uri.toString().isNotBlank()) { "uri is blank" }

        val storagePath = "${BOOKS}/$publishedId/${EPISODES}/$zipFileName"

        val ref = storage.reference.child(storagePath)

        val metadata = StorageMetadata.Builder()
            .setContentType("application/zip")
            .build()

        ref.putFile(uri, metadata).await()
    }

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

    override suspend fun downloadBookZipToCache(
        publishedId: String,
        cacheFile: File
    ): File = suspendCancellableCoroutine { cont ->

        val storagePath = "${BOOKS}/$publishedId/${EPISODES}/$zipFileName"
        val ref = storage.reference.child(storagePath)

        val downloadTask = ref.getFile(cacheFile)

        downloadTask
            .addOnSuccessListener {
                if (cont.isActive) cont.resume(cacheFile)
            }
            .addOnFailureListener { exception ->
                if (cont.isActive) cont.resumeWithException(exception)
            }

        cont.invokeOnCancellation {
            downloadTask.cancel()
            if (cacheFile.exists()) cacheFile.delete()
        }
    }
}