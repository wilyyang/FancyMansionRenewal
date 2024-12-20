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
            KeywordModel(id = 1005, category = "장르", name = "스릴러"),
            KeywordModel(id = 1006, category = "장르", name = "공포"),
            KeywordModel(id = 1007, category = "장르", name = "SF"),
            KeywordModel(id = 1008, category = "장르", name = "미스터리"),
            KeywordModel(id = 1009, category = "장르", name = "코미디"),
            KeywordModel(id = 1010, category = "장르", name = "역사"),

            KeywordModel(id = 2001, category = "주제", name = "우정"),
            KeywordModel(id = 2002, category = "주제", name = "자기 발견"),
            KeywordModel(id = 2003, category = "주제", name = "용기"),
            KeywordModel(id = 2004, category = "주제", name = "생물 다양성"),
            KeywordModel(id = 2005, category = "주제", name = "성장 이야기"),
            KeywordModel(id = 2006, category = "주제", name = "상상력"),
            KeywordModel(id = 2007, category = "주제", name = "공동체"),
            KeywordModel(id = 2008, category = "주제", name = "도전"),
            KeywordModel(id = 2009, category = "주제", name = "희생"),
            KeywordModel(id = 2010, category = "주제", name = "사랑"),
            KeywordModel(id = 2011, category = "주제", name = "배신"),
            KeywordModel(id = 2012, category = "주제", name = "복수"),
            KeywordModel(id = 2013, category = "주제", name = "자유"),
            KeywordModel(id = 2014, category = "주제", name = "권력"),
            KeywordModel(id = 2015, category = "주제", name = "희망"),
            KeywordModel(id = 2016, category = "주제", name = "절망"),

            KeywordModel(id = 3001, category = "스타일", name = "리얼리즘"),
            KeywordModel(id = 3002, category = "스타일", name = "서정적"),
            KeywordModel(id = 3003, category = "스타일", name = "풍자적"),
            KeywordModel(id = 3004, category = "스타일", name = "몽환적"),
            KeywordModel(id = 3005, category = "스타일", name = "역설적"),
            KeywordModel(id = 3006, category = "스타일", name = "비극적"),
            KeywordModel(id = 3007, category = "스타일", name = "희극적"),
            KeywordModel(id = 3008, category = "스타일", name = "서사적"),

            KeywordModel(id = 4001, category = "배경", name = "중세"),
            KeywordModel(id = 4002, category = "배경", name = "현대"),
            KeywordModel(id = 4003, category = "배경", name = "근미래"),
            KeywordModel(id = 4004, category = "배경", name = "고대"),
            KeywordModel(id = 4005, category = "배경", name = "외계"),
            KeywordModel(id = 4006, category = "배경", name = "가상세계"),
            KeywordModel(id = 4007, category = "배경", name = "디스토피아"),
            KeywordModel(id = 4008, category = "배경", name = "유토피아")
        )
    }
}