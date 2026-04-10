package com.fancymansion.data.datasource.firebase.database.user.model

data class NicknameStoreData(
    val uid: String
) {
    companion object Fields {
        const val UID = "uid"
    }
}