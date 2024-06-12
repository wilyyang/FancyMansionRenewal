package com.fancymansion.data.controller.file

import android.content.Context
import com.fancymansion.domain.interfaceController.FileController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileControllerImpl @Inject constructor(
    @ApplicationContext val context : Context
) : FileController
