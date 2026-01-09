package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.PageSettingModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCasePageSetting @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun getPageSetting(userId: String, mode: String, bookId: String) : PageSettingModel? =
        withContext(dispatcher)
        {
            bookLocalRepository.getPageSetting(userId, mode, bookId)
        }

    fun getPageSettingFlow(userId: String, mode: String, bookId: String) : Flow<PageSettingModel?> = bookLocalRepository.getPageSettingFlow(userId, mode, bookId)

    suspend fun savePageSetting(userId: String, mode: String, bookId: String, pageSetting: PageSettingModel) =
        withContext(dispatcher)
        {
            val setting = bookLocalRepository.getPageSetting(userId, mode, bookId)
            if(setting == null){
                bookLocalRepository.insertPageSetting(userId, mode, bookId, pageSetting)
            }else{
                bookLocalRepository.updatePageSetting(userId, mode, bookId, pageSetting)
            }
        }

    suspend fun deletePageSetting(userId: String, mode: String, bookId: String) =
        withContext(dispatcher)
        {
            bookLocalRepository.deletePageSettingByBookId(userId, mode, bookId)
        }
}