package com.fancymansion.data.repository

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.testBookRef
import com.fancymansion.data.R
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.model.asData
import com.fancymansion.data.datasource.appStorage.book.model.asModel
import com.fancymansion.data.datasource.database.source.book.dao.BookDatabaseDao
import com.fancymansion.data.sample.config
import com.fancymansion.data.sample.content
import com.fancymansion.data.sample.logic
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


    /**
     * Sample
     */
    override suspend fun makeSampleBookFile() {
        bookStorageSource.makeBookDir(testBookRef)

        bookStorageSource.makeConfig(testBookRef, config.asData())
        bookStorageSource.makeLogic(testBookRef, logic.asData())

        content.pages.forEach {
            bookStorageSource.makePage(testBookRef, "${it.id}", it.asData())
        }

        val imageList = listOf(
            "test_book_id_img_1.png" to R.drawable.test_book_id_img_1,
            "test_book_id_img_2.png" to R.drawable.test_book_id_img_2,
            "test_book_id_img_3.png" to R.drawable.test_book_id_img_3,
            "test_book_id_img_4.png" to R.drawable.test_book_id_img_4,
            "test_book_id_img_5.png" to R.drawable.test_book_id_img_5,
            "test_book_id_img_6.png" to R.drawable.test_book_id_img_6,
            "test_book_id_img_7.png" to R.drawable.test_book_id_img_7,
            "test_book_id_img_8.png" to R.drawable.test_book_id_img_8,
            "test_book_id_img_9.png" to R.drawable.test_book_id_img_9,
            "test_book_id_img_10.png" to R.drawable.test_book_id_img_10,
            "test_book_id_img_11.png" to R.drawable.test_book_id_img_11,
            "test_book_id_img_12.png" to R.drawable.test_book_id_img_12,
            "test_book_id_img_13.png" to R.drawable.test_book_id_img_13,
            "test_book_id_img_14.png" to R.drawable.test_book_id_img_14,
            "test_book_id_img_15.png" to R.drawable.test_book_id_img_15,
            "test_book_id_img_16.png" to R.drawable.test_book_id_img_16,
            "test_book_id_img_17.png" to R.drawable.test_book_id_img_17,
            "test_book_id_img_18.png" to R.drawable.test_book_id_img_18,
            "test_book_id_img_19.png" to R.drawable.test_book_id_img_19,
            "test_book_id_img_20.png" to R.drawable.test_book_id_img_20,
            "test_book_id_img_21.png" to R.drawable.test_book_id_img_21,
            "test_book_id_img_22.png" to R.drawable.test_book_id_img_22,
            "test_book_id_img_23.png" to R.drawable.test_book_id_img_23,
            "test_book_id_img_24.png" to R.drawable.test_book_id_img_24,
            "test_book_id_img_25.png" to R.drawable.test_book_id_img_25,
            "test_book_id_img_26.png" to R.drawable.test_book_id_img_26,
            "test_book_id_img_27.png" to R.drawable.test_book_id_img_27,
            "test_book_id_img_28.png" to R.drawable.test_book_id_img_28,
            "test_book_id_img_29.png" to R.drawable.test_book_id_img_29,
            "test_book_id_img_30.png" to R.drawable.test_book_id_img_30,
            "test_book_id_img_31.png" to R.drawable.test_book_id_img_31,
            "test_book_id_img_32.png" to R.drawable.test_book_id_img_32,
            "test_book_id_img_33.png" to R.drawable.test_book_id_img_33,
            "test_book_id_img_34.png" to R.drawable.test_book_id_img_34,
            "test_book_id_img_35.png" to R.drawable.test_book_id_img_35,
            "test_book_id_img_36.png" to R.drawable.test_book_id_img_36,
            "test_book_id_img_37.png" to R.drawable.test_book_id_img_37,
        )

        imageList.forEach {
            bookStorageSource.makeImageFromResource(testBookRef, it.first, it.second)
        }
    }
}