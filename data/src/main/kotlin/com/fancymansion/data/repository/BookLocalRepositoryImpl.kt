package com.fancymansion.data.repository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.model.asData
import com.fancymansion.data.datasource.appStorage.book.model.asModel
import com.fancymansion.data.datasource.database.source.book.dao.BookDatabaseDao
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.PageSettingModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookLocalRepositoryImpl @Inject constructor(
    private val bookStorageSource: BookStorageSource,
    private val bookDatabaseDao: BookDatabaseDao
) : BookLocalRepository {

    private var temp : PageSettingModel = PageSettingModel()
    private val tempChannel : Channel<PageSettingModel> = Channel()

    private val tempFlow = tempChannel.receiveAsFlow()

    override suspend fun getEpisodePageSetting(episodeRef: EpisodeRef): PageSettingModel? {
        return temp
    }

    override fun getEpisodePageSettingFlow(episodeRef: EpisodeRef): Flow<PageSettingModel> {
        return tempFlow
    }

    override suspend fun saveEpisodePageSetting(episodeRef: EpisodeRef, pageSetting: PageSettingModel) {
        temp = pageSetting
        tempChannel.send(temp)
    }

    override suspend fun deleteEpisodePageSetting(episodeRef: EpisodeRef) {
        // TODO
    }

    /**
     * Init
     */
    override suspend fun makeUserDir(userId: String) {
        bookStorageSource.makeUserDir(userId)
    }

    override suspend fun deleteUserDir(userId: String) {
        bookStorageSource.deleteUserDir(userId)
    }

    override suspend fun makeBookDir(userId: String, mode: ReadMode, bookId: String) {
        bookStorageSource.makeBookDir(userId, mode, bookId)
    }

    override suspend fun deleteBookDir(userId: String, mode: ReadMode, bookId: String) {
        bookStorageSource.deleteBookDir(userId, mode, bookId)
    }

    override suspend fun makeEpisodeDir(episodeRef: EpisodeRef) {
        bookStorageSource.makeEpisodeDir(episodeRef)
    }

    override suspend fun deleteEpisodeDir(episodeRef: EpisodeRef) {
        bookStorageSource.deleteEpisodeDir(episodeRef)
    }

    /**
     * File
     */
    override suspend fun makeBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String,
        bookInfo: BookInfoModel
    ): Boolean {
        return bookStorageSource.makeBookInfo(userId, mode, bookId, bookInfo.asData())
    }

    override suspend fun loadBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String
    ): BookInfoModel {
        return bookStorageSource.loadBookInfo(userId, mode, bookId).asModel()
    }

    override suspend fun makeEpisodeInfo(
        episodeRef: EpisodeRef,
        episodeInfo: EpisodeInfoModel
    ): Boolean {
        return bookStorageSource.makeEpisodeInfo(episodeRef, episodeInfo.asData())
    }

    override suspend fun loadEpisodeInfo(episodeRef: EpisodeRef): EpisodeInfoModel {
        return bookStorageSource.loadEpisodeInfo(episodeRef).asModel()
    }

    override suspend fun makeLogic(episodeRef: EpisodeRef, logic: LogicModel): Boolean {
        return bookStorageSource.makeLogic(episodeRef, logic.asData())
    }

    override suspend fun loadLogic(episodeRef: EpisodeRef): LogicModel {
        return bookStorageSource.loadLogic(episodeRef).asModel()
    }

    override suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageModel): Boolean {
        return bookStorageSource.makePage(episodeRef, pageId, page.asData())
    }

    override suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageModel {
        return bookStorageSource.loadPage(episodeRef, pageId).asModel()
    }

    override suspend fun loadPageImage(episodeRef: EpisodeRef, imageName: String): File {
        return bookStorageSource.loadPageImage(episodeRef, imageName)
    }

    override suspend fun loadEpisodeThumbnail(episodeRef: EpisodeRef, imageName: String): File {
        return bookStorageSource.loadEpisodeThumbnail(episodeRef, imageName)
    }

    override suspend fun loadCoverImage(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String
    ): File {
        return bookStorageSource.loadCoverImage(userId, mode, bookId, imageName)
    }

    override suspend fun makePageImageFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    ){
        return bookStorageSource.makePageImageFromResource(episodeRef, imageName, resourceId)
    }

    override suspend fun makeEpisodeThumbnailFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    ) {
        return bookStorageSource.makeEpisodeThumbnailFromResource(episodeRef, imageName, resourceId)
    }

    override suspend fun makeCoverImageFromResource(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String,
        resourceId: Int
    ) {
        return bookStorageSource.makeCoverImageFromResource(userId, mode, bookId, imageName, resourceId)
    }

    /**
     * Database
     */

    override suspend fun deleteActionCountByEpisode(episodeRef: EpisodeRef) {
        bookDatabaseDao.deleteActionCountByEpisode(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId)
    }

    override suspend fun updateActionCount(episodeRef: EpisodeRef, actionId: Long, newCount : Int){
        bookDatabaseDao.updateActionCount(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId, actionId, newCount)
    }
    override suspend fun insertActionCount(episodeRef: EpisodeRef, actionId: Long){
        bookDatabaseDao.insertActionCount(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId, actionId)
    }

    override suspend fun getActionCount(episodeRef: EpisodeRef, actionId: Long) : Int? {
        return bookDatabaseDao.getActionCount(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId, actionId)
    }

    override suspend fun deleteReadingProgressByEpisode(episodeRef: EpisodeRef) {
        bookDatabaseDao.deleteReadingProgressByEpisode(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId)
    }

    override suspend fun getReadingProgressPageId(episodeRef: EpisodeRef): Long? {
        return bookDatabaseDao.getReadingProgressPageId(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId)
    }

    override suspend fun insertReadingProgress(episodeRef: EpisodeRef, pageId: Long) {
        bookDatabaseDao.insertReadingProgress(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId, pageId)
    }

    override suspend fun updateReadingProgressPageId(episodeRef: EpisodeRef, newPageId: Long) {
        bookDatabaseDao.updateReadingProgressPageId(episodeRef.userId, episodeRef.mode.name, episodeRef.bookId, episodeRef.episodeId, newPageId)
    }
}