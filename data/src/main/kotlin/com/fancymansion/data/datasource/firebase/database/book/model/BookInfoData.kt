package com.fancymansion.data.datasource.firebase.database.book.model

data class BookInfoData (
    val bookId: String
){
    companion object Fields {
        const val BOOK_ID = "bookId"
    }
}