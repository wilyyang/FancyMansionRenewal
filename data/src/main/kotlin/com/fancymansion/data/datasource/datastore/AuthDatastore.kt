package com.fancymansion.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthDatastore constructor( private val context : Context) {

    private val dataStore : DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile =
            { context.preferencesDataStoreFile( "auth") }
        )

    /**
     * TOKEN
     * : API 호출 시, 업데이트
     */
    private val keyToken = stringPreferencesKey("KEY_TOKEN")

    suspend fun getToken() : String = dataStore.data.first()[keyToken] ?: ""
    suspend fun getTokenFlow(): Flow<String> = dataStore.data.map { preferences ->
        preferences[keyToken] ?: ""
    }

    suspend fun setToken(value : String){
        dataStore.edit { pref ->
            pref[keyToken] = value
        }
    }
}