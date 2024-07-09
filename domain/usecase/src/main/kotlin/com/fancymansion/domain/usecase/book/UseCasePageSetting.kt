package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.PageSettingModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCasePageSetting @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun getPageSetting(bookRef: BookRef) : PageSettingModel? =
        withContext(dispatcher)
        {
            bookLocalRepository.getPageSetting(bookRef)
        }

    fun getPageSettingFlow(bookRef: BookRef) : Flow<PageSettingModel> = bookLocalRepository.getPageSettingFlow(bookRef)

    suspend fun savePageSetting(bookRef: BookRef, pageSetting: PageSettingModel) =
        withContext(dispatcher)
        {
            bookLocalRepository.savePageSetting(bookRef, pageSetting)
        }

    suspend fun deletePageSetting(bookRef: BookRef) =
        withContext(dispatcher)
        {
            bookLocalRepository.deletePageSetting(bookRef)
        }
}