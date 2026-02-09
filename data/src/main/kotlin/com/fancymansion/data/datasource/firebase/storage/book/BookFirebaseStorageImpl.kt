package com.fancymansion.data.datasource.firebase.storage.book

import android.net.Uri
import com.fancymansion.data.datasource.firebase.StorageCollections.BOOKS
import com.fancymansion.data.datasource.firebase.StorageCollections.COVERS
import com.fancymansion.data.datasource.firebase.StorageCollections.EPISODES
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BookFirebaseStorageImpl(
    private val storage: FirebaseStorage
) : BookFirebaseStorage {

    override suspend fun uploadEpisodeZipFile(publishedId: String, version: Int, uri: Uri) {
        require(publishedId.isNotBlank()) { "publishedId is blank" }
        require(uri.toString().isNotBlank()) { "uri is blank" }

        val storagePath = "${BOOKS}/$publishedId/${EPISODES}/${getZipFileName(version)}"

        val ref = storage.reference.child(storagePath)

        val metadata = StorageMetadata.Builder()
            .setContentType("application/zip")
            .build()

        ref.putFile(uri, metadata).await()
    }

    override suspend fun deleteBookStorageByPublishedId(publishedId: String) {
        require(publishedId.isNotBlank()) { "publishedId is blank" }

        deleteAllUnder(storage.reference.child("${BOOKS}/$publishedId/${COVERS}"))
        deleteAllUnder(storage.reference.child("${BOOKS}/$publishedId/${EPISODES}"))
    }

    override suspend fun pruneEpisodeZipFile(
        publishedId: String,
        currentVersion: Int,
        keep: Int
    ) {
        require(publishedId.isNotBlank()) { "publishedId is blank" }
        require(currentVersion >= 0) { "currentVersion is invalid" }
        require(keep > 0) { "keep must be > 0" }

        val deleteVersion = currentVersion - keep
        if (deleteVersion < 0) return

        try {
            storage.reference
                .child("${BOOKS}/$publishedId/${EPISODES}/${getZipFileName(deleteVersion)}")
                .delete()
                .await()
        } catch (e: StorageException) {
            if (e.errorCode != StorageException.ERROR_OBJECT_NOT_FOUND) {
                throw e
            }
        }
    }

    private suspend fun deleteAllUnder(ref: StorageReference) {
        val result = ref.listAll().await()
        result.items.forEach { it.delete().await() }
        result.prefixes.forEach { deleteAllUnder(it) }
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
        version: Int,
        cacheFile: File
    ): File = suspendCancellableCoroutine { cont ->

        val storagePath = "${BOOKS}/$publishedId/${EPISODES}/${getZipFileName(version)}"
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

    override suspend fun getBookCoverImageUrl(publishedId: String, imageFileName: String): String {
        val storagePath = "${BOOKS}/$publishedId/${COVERS}/$imageFileName"
        val ref = storage.reference.child(storagePath)
        return ref.downloadUrl.await().toString()
    }

    private fun getZipFileName(version: Int) = "book.v${version}.zip"
}