package com.fancymansion.data.datasource.dataStore.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppStoreSourceImpl(context : Context) : AppStoreSource{
    private val appContext = context.applicationContext

    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            produceFile = { appContext.preferencesDataStoreFile("app_store") }
        )
    private val KEY_IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")

    override suspend fun isFirstLaunch(): Boolean {
        return dataStore.data
            .map { prefs -> prefs[KEY_IS_FIRST_LAUNCH] ?: true }
            .first()
    }

    override suspend fun markLaunched() {
        dataStore.edit { prefs ->
            prefs[KEY_IS_FIRST_LAUNCH] = false
        }
    }
}