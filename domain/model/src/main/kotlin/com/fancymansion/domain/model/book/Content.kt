package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.PageId

/**
 * Content
 */
sealed class Source {
    data class Text(val description: String) : Source()
//    data class Image(val filePath: String) : Source()

    // 임시
    data class Image(val resId: Int) : Source()
}


data class Content(
    val pages: List<Page>
)

data class Page(
    val id: PageId,
    val title: String,
    val sources: List<Source>
)