package com.fancymansion.data.datasource.dataStore.user

interface UserStoreSource {
    suspend fun getPublishedBookIds(): Set<String>
    suspend fun replacePublishedBookIds(ids: Set<String>)
    suspend fun addPublishedBookId(bookId: String)
    suspend fun removePublishedBookId(bookId: String)
    suspend fun clearPublishedBookIds()
}