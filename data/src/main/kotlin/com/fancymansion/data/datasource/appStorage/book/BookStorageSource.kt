package com.fancymansion.data.datasource.appStorage.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.data.datasource.appStorage.book.model.ConfigData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import java.io.File

interface BookStorageSource {
    /**
     * Dir
     */
    suspend fun makeUserDir(userId: String)

    suspend fun deleteUserDir(userId: String)

    suspend fun makeBookDir(bookRef: BookRef)

    suspend fun deleteBookDir(bookRef: BookRef)

    /**
     * File
     */
    suspend fun makeConfig(bookRef: BookRef, config: ConfigData): Boolean

    suspend fun loadConfig(bookRef: BookRef): ConfigData

    suspend fun makeLogic(bookRef: BookRef, logic: LogicData): Boolean

    suspend fun loadLogic(bookRef: BookRef): LogicData

    suspend fun makePage(bookRef: BookRef, pageId: String, page: PageData): Boolean

    suspend fun loadPage(bookRef: BookRef, pageId: String): PageData

    suspend fun loadImage(bookRef: BookRef, imageName: String) : File

    suspend fun loadCover(bookRef: BookRef, coverName: String) : File

    suspend fun makeImageFromResource(
        bookRef: BookRef,
        imageName: String,
        resourceId: Int
    )

    suspend fun makeCoverFromResource(
        bookRef: BookRef,
        coverName: String,
        resourceId: Int
    )
}