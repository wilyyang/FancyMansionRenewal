package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
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
    suspend fun getEpisodePageSetting(episodeRef: EpisodeRef) : PageSettingModel? =
        withContext(dispatcher)
        {
            bookLocalRepository.getEpisodePageSetting(episodeRef)
        }

    fun getEpisodePageSettingFlow(episodeRef: EpisodeRef) : Flow<PageSettingModel> = bookLocalRepository.getEpisodePageSettingFlow(episodeRef)

    suspend fun saveEpisodePageSetting(episodeRef: EpisodeRef, pageSetting: PageSettingModel) =
        withContext(dispatcher)
        {
            bookLocalRepository.saveEpisodePageSetting(episodeRef, pageSetting)
        }

    suspend fun deleteEpisodePageSetting(episodeRef: EpisodeRef) =
        withContext(dispatcher)
        {
            bookLocalRepository.deleteEpisodePageSetting(episodeRef)
        }
}