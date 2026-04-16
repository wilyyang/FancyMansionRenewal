package com.fancymansion.data.datasource.dataStore.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.fancymansion.data.datasource.dataStore.user.model.LocalBookRefData
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

class UserStoreSourceImpl(context : Context) : UserStoreSource{

    private val appContext = context.applicationContext
    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            produceFile = { appContext.preferencesDataStoreFile("user_store") }
        )

    private val PUBLISHED_BOOK_REFS =
        stringSetPreferencesKey("published_book_refs")

    private val gson = Gson()

    private fun LocalBookRefData.toJson(): String {
        return gson.toJson(this)
    }

    private fun String.toLocalBookRefData(): LocalBookRefData {
        return gson.fromJson(this, LocalBookRefData::class.java)
    }

    override suspend fun getPublishedBookRefs(): Set<LocalBookRefData> {
        return dataStore.data
            .first()[PUBLISHED_BOOK_REFS]
            ?.map { it.toLocalBookRefData() }
            ?.toSet()
            ?: emptySet()
    }

    override suspend fun replacePublishedBookRefs(bookRefs: Set<LocalBookRefData>) {
        dataStore.edit { prefs ->
            prefs[PUBLISHED_BOOK_REFS] = bookRefs.map { it.toJson() }.toSet()
        }
    }

    override suspend fun addPublishedBookRef(bookRef: LocalBookRefData) {
        dataStore.edit { prefs ->
            val current = prefs[PUBLISHED_BOOK_REFS] ?: emptySet()
            prefs[PUBLISHED_BOOK_REFS] = current + bookRef.toJson()
        }
    }

    override suspend fun updatePublishedBookRef(
        oldRef: LocalBookRefData,
        newRef: LocalBookRefData
    ) {
        dataStore.edit { prefs ->
            val current = prefs[PUBLISHED_BOOK_REFS] ?: emptySet()

            val updated = current
                .map { it.toLocalBookRefData() }
                .filterNot { it.bookId == oldRef.bookId }
                .plus(newRef)
                .map { it.toJson() }
                .toSet()

            prefs[PUBLISHED_BOOK_REFS] = updated
        }
    }

    override suspend fun removePublishedBookRef(bookRef: LocalBookRefData) {
        dataStore.edit { prefs ->
            val current = prefs[PUBLISHED_BOOK_REFS] ?: emptySet()

            val updated = current.filterNot {
                it.toLocalBookRefData().bookId == bookRef.bookId
            }.toSet()

            prefs[PUBLISHED_BOOK_REFS] = updated
        }
    }

    override suspend fun clearPublishedBookRefs() {
        dataStore.edit { prefs ->
            prefs.remove(PUBLISHED_BOOK_REFS)
        }
    }
}