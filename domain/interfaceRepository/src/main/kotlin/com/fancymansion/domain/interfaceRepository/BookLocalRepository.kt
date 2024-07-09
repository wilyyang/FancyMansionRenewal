package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.domain.model.book.ConfigModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.PageSettingModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface BookLocalRepository {
    suspend fun getPageSetting(bookRef: BookRef): PageSettingModel?
    fun getPageSettingFlow(bookRef: BookRef): Flow<PageSettingModel>
    suspend fun savePageSetting(bookRef: BookRef, pageSetting: PageSettingModel)
    suspend fun deletePageSetting(bookRef: BookRef)

    suspend fun makeUserDir(userId: String)
    suspend fun deleteUserDir(userId: String)
    suspend fun makeBookDir(bookRef: BookRef)
    suspend fun deleteBookDir(bookRef: BookRef)

    suspend fun makeConfig(bookRef: BookRef, config: ConfigModel): Boolean
    suspend fun loadConfig(bookRef: BookRef): ConfigModel
    suspend fun makeLogic(bookRef: BookRef, logic: LogicModel): Boolean
    suspend fun loadLogic(bookRef: BookRef): LogicModel
    suspend fun makePage(bookRef: BookRef, pageId: Long, page: PageModel): Boolean
    suspend fun loadPage(bookRef: BookRef, pageId: Long): PageModel
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

    suspend fun deleteActionCountByBook(bookRef: BookRef)
    suspend fun updateActionCount(bookRef: BookRef, actionId: Long, newCount : Int)
    suspend fun insertActionCount(bookRef: BookRef, actionId: Long)
    suspend fun getActionCount(bookRef: BookRef, actionId: Long) : Int?

    suspend fun deleteReadingProgressByBook(bookRef: BookRef)
    suspend fun getReadingProgressPageId(bookRef: BookRef): Long?
    suspend fun insertReadingProgress(bookRef: BookRef, pageId: Long)
    suspend fun updateReadingProgressPageId(bookRef: BookRef, newPageId: Long)
}