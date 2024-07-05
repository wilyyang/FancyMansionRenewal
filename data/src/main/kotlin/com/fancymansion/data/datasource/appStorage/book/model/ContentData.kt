package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.domain.model.book.ContentModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.SourceModel
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext

/**
 * Content
 */
sealed class SourceData(val type : String) {
    data class TextData(val description: String) : SourceData("text")
    data class ImageData(val imageName: String) : SourceData("image")

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
                "text" -> TextData(jsonObject.get("description").asString)
                "image" -> ImageData(jsonObject.get("imageName").asString)
                else -> throw IllegalArgumentException("Unknown SourceData type: $type")
            }
        }
    }
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