package com.fancymansion.di.injectController

import com.fancymansion.data.controller.file.FileControllerImpl
import com.fancymansion.domain.interfaceController.FileController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HiltController {

    @Binds
    fun bindFileController(controller : FileControllerImpl) : FileController
}