package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.KeywordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseGetTotalKeyword @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        listOf(
            KeywordModel(id = 1001, category = "장르", name = "판타지"),
            KeywordModel(id = 1002, category = "장르", name = "로맨스"),
            KeywordModel(id = 1003, category = "장르", name = "모험"),
            KeywordModel(id = 1004, category = "장르", name = "드라마"),

            KeywordModel(id = 2001, category = "주제", name = "우정"),
            KeywordModel(id = 2002, category = "주제", name = "자기 발견"),
            KeywordModel(id = 2003, category = "주제", name = "용기"),
            KeywordModel(id = 2004, category = "주제", name = "생물 다양성"),
            KeywordModel(id = 2005, category = "주제", name = "성장 이야기"),
            KeywordModel(id = 2006, category = "주제", name = "상상력"),
            KeywordModel(id = 2007, category = "주제", name = "공동체"),
            KeywordModel(id = 2008, category = "주제", name = "도전"),
        )
    }
}