package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.domain.model.book.ContentModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.SourceModel
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext

const val TYPE_SOURCE_TEXT = "text"
const val TYPE_SOURCE_IMAGE = "image"

/**
 * Content
 */
data class ContentData(
    val pages: List<PageData>
)
data class PageData(
    val id: Long,
    val title: String,
    val sources: List<SourceData>
)
sealed class SourceData(val type : String) {
    data class TextData(val description: String) : SourceData(TYPE_SOURCE_TEXT)
    data class ImageData(val imageName: String) : SourceData(TYPE_SOURCE_IMAGE)

    fun toJson(context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("type", type)
        when (this) {
            is TextData -> jsonObject.addProperty("description", description)
            is ImageData -> jsonObject.addProperty("imageName", imageName)
        }
        return jsonObject
    }

    companion object {
        fun fromJson(json: JsonElement, context: JsonDeserializationContext): SourceData {
            if (!json.isJsonObject) {
                throw IllegalArgumentException("Invalid JSON format for SourceData")
            }
            val jsonObject = json.asJsonObject
            return when (val type = jsonObject.get("type").asString) {
                TYPE_SOURCE_TEXT -> TextData(jsonObject.get("description").asString)
                TYPE_SOURCE_IMAGE -> ImageData(jsonObject.get("imageName").asString)
                else -> throw IllegalArgumentException("Unknown SourceData type: $type")
            }
        }
    }
}
fun ContentData.asModel() = ContentModel(
    pages = pages.map { it.asModel() }
)

fun ContentModel.asData() = ContentData(
    pages = pages.map { it.asData() }
)
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
fun SourceData.asModel(): SourceModel =
    when (this) {
        is SourceData.TextData -> {
            SourceModel.TextModel(
                description = description
            )
        }

        is SourceData.ImageData -> {
            SourceModel.ImageModel(
                imageName = imageName
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
                imageName = imageName
            )
        }
    }

