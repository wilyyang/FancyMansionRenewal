package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.domain.model.book.ConfigModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import java.io.File

interface BookLocalRepository {
    suspend fun makeUserDir(userId: String)
    suspend fun deleteUserDir(userId: String)
    suspend fun makeBookDir(bookRef: BookRef)
    suspend fun deleteBookDir(bookRef: BookRef)

    suspend fun makeConfig(bookRef: BookRef, config: ConfigModel): Boolean
    suspend fun loadConfig(bookRef: BookRef): ConfigModel
    suspend fun makeLogic(bookRef: BookRef, logic: LogicModel): Boolean
    suspend fun loadLogic(bookRef: BookRef): LogicModel
    suspend fun makePage(bookRef: BookRef, pageId: String, page: PageModel): Boolean
    suspend fun loadPage(bookRef: BookRef, pageId: String): PageModel
    suspend fun loadImage(bookRef: BookRef, imageName: String) : File
    suspend fun loadCover(bookRef: BookRef, coverName: String) : File

    suspend fun deleteActionCountByBook(bookRef: BookRef)
    suspend fun updateActionCount(bookRef: BookRef, actionId: Long, newCount : Int)
    suspend fun insertActionCount(bookRef: BookRef, actionId: Long)
    suspend fun getActionCount(bookRef: BookRef, actionId: Long) : Int?

    suspend fun makeSampleBookFile()
}