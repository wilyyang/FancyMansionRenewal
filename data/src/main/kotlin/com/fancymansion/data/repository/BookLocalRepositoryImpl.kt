package com.fancymansion.data.repository

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.model.asData
import com.fancymansion.data.datasource.appStorage.book.model.asModel
import com.fancymansion.data.datasource.database.source.book.dao.BookDatabaseDao
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.ConfigModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookLocalRepositoryImpl @Inject constructor(
    private val bookStorageSource: BookStorageSource,
    private val bookDatabaseDao: BookDatabaseDao
) : BookLocalRepository {

    /**
     * Init
     */
    override suspend fun makeUserDir(userId: String) {
        bookStorageSource.makeUserDir(userId)
    }

    override suspend fun deleteUserDir(userId: String) {
        bookStorageSource.deleteUserDir(userId)
    }

    override suspend fun makeBookDir(bookRef: BookRef) {
        bookStorageSource.makeBookDir(bookRef)
    }

    override suspend fun deleteBookDir(bookRef: BookRef) {
        bookStorageSource.deleteBookDir(bookRef)
    }


    /**
     * File
     */
    override suspend fun makeConfig(bookRef: BookRef, config: ConfigModel): Boolean {
        return bookStorageSource.makeConfig(bookRef, config.asData())
    }

    override suspend fun loadConfig(bookRef: BookRef): ConfigModel {
        return bookStorageSource.loadConfig(bookRef).asModel()
    }

    override suspend fun makeLogic(bookRef: BookRef, logic: LogicModel): Boolean {
        return bookStorageSource.makeLogic(bookRef, logic.asData())
    }

    override suspend fun loadLogic(bookRef: BookRef): LogicModel {
        return bookStorageSource.loadLogic(bookRef).asModel()
    }

    override suspend fun makePage(bookRef: BookRef, pageId: String, page: PageModel): Boolean {
        return bookStorageSource.makePage(bookRef, pageId, page.asData())
    }

    override suspend fun loadPage(bookRef: BookRef, pageId: String): PageModel {
        return bookStorageSource.loadPage(bookRef, pageId).asModel()
    }

    override suspend fun loadImage(bookRef: BookRef, imageName: String): File {
        return bookStorageSource.loadImage(bookRef, imageName)
    }

    override suspend fun loadCover(bookRef: BookRef, coverName: String): File {
        return bookStorageSource.loadCover(bookRef, coverName)
    }

    override suspend fun makeImageFromResource(
        bookRef: BookRef,
        imageName: String,
        resourceId: Int
    ){
        bookStorageSource.makeImageFromResource(bookRef, imageName, resourceId)
    }

    override suspend fun makeCoverFromResource(
        bookRef: BookRef,
        coverName: String,
        resourceId: Int
    ){
        bookStorageSource.makeCoverFromResource(bookRef, coverName, resourceId)
    }

    /**
     * Database
     */

    override suspend fun deleteActionCountByBook(bookRef: BookRef) {
        bookDatabaseDao.deleteActionCountByBook(bookRef.userId, bookRef.mode.name, bookRef.bookId)
    }

    override suspend fun updateActionCount(bookRef: BookRef, actionId: Long, newCount : Int){
        bookDatabaseDao.updateActionCount(bookRef.userId, bookRef.mode.name, bookRef.bookId, "$actionId", newCount)
    }
    override suspend fun insertActionCount(bookRef: BookRef, actionId: Long){
        bookDatabaseDao.insertActionCount(bookRef.userId, bookRef.mode.name, bookRef.bookId, "$actionId")
    }

    override suspend fun getActionCount(bookRef: BookRef, actionId: Long) : Int? {
        return bookDatabaseDao.getActionCount(bookRef.userId, bookRef.mode.name, bookRef.bookId, "$actionId")
    }

    override suspend fun deleteReadingProgressByBook(bookRef: BookRef) {
        bookDatabaseDao.deleteReadingProgressByBook(bookRef.userId, bookRef.mode.name, bookRef.bookId)
    }

    override suspend fun getReadingProgressPageId(bookRef: BookRef): String? {
        return bookDatabaseDao.getReadingProgressPageId(bookRef.userId, bookRef.mode.name, bookRef.bookId)
    }

    override suspend fun insertReadingProgress(bookRef: BookRef, pageId: String) {
        bookDatabaseDao.insertReadingProgress(bookRef.userId, bookRef.mode.name, bookRef.bookId, pageId)
    }

    override suspend fun updateReadingProgressPageId(bookRef: BookRef, newPageId: String) {
        bookDatabaseDao.updateReadingProgressPageId(bookRef.userId, bookRef.mode.name, bookRef.bookId, newPageId)
    }
}