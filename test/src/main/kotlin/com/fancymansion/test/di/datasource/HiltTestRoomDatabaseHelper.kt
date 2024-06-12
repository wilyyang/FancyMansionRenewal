package com.fancymansion.test.di.datasource

import com.fancymansion.data.datasource.database.base.di.HiltRoomDatabaseHelper
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltRoomDatabaseHelper::class],
)
class HiltTestRoomDatabaseHelper {

}