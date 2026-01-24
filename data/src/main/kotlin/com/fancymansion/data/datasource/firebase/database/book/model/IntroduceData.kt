package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.book.IntroduceModel
import com.fancymansion.domain.model.book.KeywordModel

data class IntroduceData(
    val title: String,
    val coverList: List<String>,
    val keywordIds: List<Long>,
    val description: String
) {
    companion object Fields {
        const val TITLE = "title"
        const val COVER_LIST = "coverList"
        const val KEYWORD_IDS = "keywordIds"
        const val DESCRIPTION = "description"
    }
}

fun IntroduceModel.asData(): IntroduceData = IntroduceData(
    title = title,
    coverList = coverList,
    keywordIds = keywordList.map { it.id },
    description = description
)

fun IntroduceData.asModel(keywordMap: Map<Long, KeywordModel>): IntroduceModel = IntroduceModel(
    title = title,
    coverList = coverList,
    keywordList = keywordIds.mapNotNull { keywordMap[it] },
    description = description
)