package com.fancymansion.data.datasource.dataStore.user

import com.fancymansion.data.datasource.dataStore.user.model.LocalBookRefData

interface UserStoreSource {
    suspend fun getPublishedBookRefs(): Set<LocalBookRefData>
    suspend fun replacePublishedBookRefs(bookRefs: Set<LocalBookRefData>)
    suspend fun addPublishedBookRef(bookRef: LocalBookRefData)
    suspend fun updatePublishedBookRef(oldRef: LocalBookRefData, newRef: LocalBookRefData)
    suspend fun removePublishedBookRef(bookRef: LocalBookRefData)
    suspend fun clearPublishedBookRefs()
}