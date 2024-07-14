package com.fancymansion.domain.model.book

/**
 * Content
 */
data class ContentModel(
    val pages: List<PageModel>
)

data class PageModel(
    val id: Long,
    val title: String,
    val sources: List<SourceModel>
)
sealed class SourceModel {
    data class TextModel(val description: String) : SourceModel()
    data class ImageModel(val imageName: String) : SourceModel()
}