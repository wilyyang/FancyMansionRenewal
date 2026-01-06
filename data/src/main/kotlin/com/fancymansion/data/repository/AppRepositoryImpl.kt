package com.fancymansion.data.repository

import com.fancymansion.data.datasource.dataStore.app.AppStoreSource
import com.fancymansion.domain.interfaceRepository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appStoreSource: AppStoreSource
) : AppRepository {

    override suspend fun isFirstLaunch(): Boolean {
        return appStoreSource.isFirstLaunch()
    }

    override suspend fun markLaunched() {
        appStoreSource.markLaunched()
    }
}