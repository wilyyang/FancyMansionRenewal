package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.model.book.keywords
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseGetTotalKeyword @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        keywords
    }
}