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
import kotlinx.coroutines.flow.first

class UserStoreSourceImpl(context : Context) : UserStoreSource{

    private val appContext = context.applicationContext

    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            produceFile = { appContext.preferencesDataStoreFile("user_store") }
        )

    private val PUBLISHED_BOOK_IDS =
        stringSetPreferencesKey("published_book_ids")

    override suspend fun getPublishedBookIds(): Set<String> {
        return dataStore.data
            .first()[PUBLISHED_BOOK_IDS]
            ?: emptySet()
    }

    override suspend fun replacePublishedBookIds(ids: Set<String>) {
        dataStore.edit { prefs ->
            prefs[PUBLISHED_BOOK_IDS] = ids
        }
    }

    override suspend fun addPublishedBookId(bookId: String) {
        dataStore.edit { prefs ->
            val current = prefs[PUBLISHED_BOOK_IDS] ?: emptySet()
            prefs[PUBLISHED_BOOK_IDS] = current + bookId
        }
    }

    override suspend fun removePublishedBookId(bookId: String) {
        dataStore.edit { prefs ->
            val current = prefs[PUBLISHED_BOOK_IDS] ?: emptySet()
            prefs[PUBLISHED_BOOK_IDS] = current - bookId
        }
    }

    override suspend fun clearPublishedBookIds() {
        dataStore.edit { prefs ->
            prefs.remove(PUBLISHED_BOOK_IDS)
        }
    }
}