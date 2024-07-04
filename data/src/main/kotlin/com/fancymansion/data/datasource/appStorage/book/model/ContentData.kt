package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.domain.model.book.ContentModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.SourceModel

/**
 * Content
 */
sealed class SourceData {
    data class TextData(val description: String) : SourceData()
//    data class Image(val filePath: String) : Source()

    // 임시
    data class ImageData(val resId: Int) : SourceData()
}

data class PageData(
    val id: Long,
    val title: String,
    val sources: List<SourceData>
)

data class ContentData(
    val pages: List<PageData>
)

fun SourceData.asModel(): SourceModel =
    when (this) {
        is SourceData.TextData -> {
            SourceModel.TextModel(
                description = description
            )
        }

        is SourceData.ImageData -> {
            SourceModel.ImageModel(
                resId = resId
            )
        }
    }

fun SourceModel.asData(): SourceData =
    when (this) {
        is SourceModel.TextModel -> {
            SourceData.TextData(
                description = description
            )
        }

        is SourceModel.ImageModel -> {
            SourceData.ImageData(
                resId = resId
            )
        }
    }

fun PageData.asModel() = PageModel(
    id = id,
    title = title,
    sources = sources.map { it.asModel() }
)

fun PageModel.asData() = PageData(
    id = id,
    title = title,
    sources = sources.map { it.asData() }
)

fun ContentData.asModel() = ContentModel(
    pages = pages.map { it.asModel() }
)

fun ContentModel.asData() = ContentData(
    pages = pages.map { it.asData() }
)