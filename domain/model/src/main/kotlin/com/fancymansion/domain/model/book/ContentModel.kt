package com.fancymansion.domain.model.book

/**
 * Content
 */
sealed class SourceModel {
    data class TextModel(val description: String) : SourceModel()
//    data class Image(val filePath: String) : Source()

    // 임시
    data class ImageModel(val resId: Int) : SourceModel()
}


data class ContentModel(
    val pages: List<PageModel>
)

data class PageModel(
    val id: Long,
    val title: String,
    val sources: List<SourceModel>
)