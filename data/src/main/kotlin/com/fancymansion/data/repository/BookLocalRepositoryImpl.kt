package com.fancymansion.data.repository

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.Logic
import com.fancymansion.domain.model.book.Page
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookLocalRepositoryImpl @Inject constructor(
) : BookLocalRepository {
    // 임시
    private var actionCountMap = mutableMapOf<Long, Int>()

    override suspend fun loadLogicFromFile(bookRef: BookRef): Logic {
        return logic
    }

    override suspend fun loadPageFromFile(
        bookRef: BookRef,
        pageId: Long
    ): Page {
        return content.pages.first { it.id == pageId }
    }

    override suspend fun makeSampleBookFile() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBookActionCount(bookRef: BookRef) {
        actionCountMap = mutableMapOf<Long, Int>()
    }

    override suspend fun incrementActionCount(
        bookRef: BookRef,
        actionId: Long
    ) {
        if (actionCountMap[actionId] != null) {
            actionCountMap[actionId] = actionCountMap[actionId]!! + 1
        } else {
            actionCountMap[actionId] = 1
        }
    }

    override suspend fun getActionCount(bookRef: BookRef, actionId: Long) : Int {
        return actionCountMap[actionId] ?: 0
    }
}