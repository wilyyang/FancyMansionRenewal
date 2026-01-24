package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.book.EditorModel

data class EditorData(
    val editorId: String,
    val editorName: String,
    val editorEmail: String
) {
    companion object Fields {
        const val EDITOR_ID = "editorId"
        const val EDITOR_NAME = "editorName"
        const val EDITOR_EMAIL = "editorEmail"
    }
}

fun EditorModel.asData(): EditorData = EditorData(
    editorId = editorId,
    editorName = editorName,
    editorEmail = editorEmail
)

fun EditorData.asModel(): EditorModel = EditorModel(
    editorId = editorId,
    editorName = editorName,
    editorEmail = editorEmail
)