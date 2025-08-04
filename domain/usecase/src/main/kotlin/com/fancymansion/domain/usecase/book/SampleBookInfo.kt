package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.IntroduceModel
import com.fancymansion.domain.model.book.KeywordModel

val sampleBookInfoList: List<Pair<BookInfoModel, EpisodeInfoModel>> = List(30) { index ->
    val bookId = "$index"
    val episodeId = "$index"

    val bookInfo = BookInfoModel(
        id = bookId,
        introduce = IntroduceModel(
            title = "샘플 북 ${index + 1}",
            coverList = listOf(),
            keywordList = listOf(
                KeywordModel(id = index * 10L + 1, category = "판타지", name = "마술"),
                KeywordModel(id = index * 10L + 2, category = "과학", name = "펑키"),
                KeywordModel(id = index * 10L + 3, category = "로맨스", name = "로맨스 코미디")
            ),
            description = "이것은 샘플 북 ${index + 1}의 설명입니다."
        ),
        editor = EditorModel(
            editorId = "editor${index + 1}",
            editorName = "에디터 ${index + 1}",
            editorEmail = "editor${index + 1}@example.com"
        )
    )

    val episodeInfo = EpisodeInfoModel(
        id = episodeId,
        bookId = bookId,
        title = "에피소드 1 - 샘플 북 ${index + 1}",
        pageCount = (10..50).random(),
        version = 1,
        createTime = System.currentTimeMillis() - (index * 1000000),
        editTime = System.currentTimeMillis(),
        readMode = ReadMode.EDIT
    )

    bookInfo to episodeInfo
}