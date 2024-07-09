package com.fancymansion.data.datasource.appStorage.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.ReadMode
import java.io.File

class BookPath {
    companion object {
        private const val BOOKS = "books"
        private const val CONTENT = "content"
        private const val MEDIA = "media"
        private const val PAGES = "pages"
        private const val CONFIG_JSON = "config.json"
        private const val LOGIC_JSON = "logic.json"

        private fun pageJson(pageId: Long) = "$pageId.json"

        fun booksPath() = BOOKS
        fun userPath(userId: String) = booksPath().joinPath(userId)
        fun modePath(userId: String, mode: ReadMode) = userPath(userId).joinPath(mode.name)
        fun bookPath(bookRef: BookRef) = modePath(bookRef.userId, bookRef.mode).joinPath(bookRef.bookId)
        fun contentPath(bookRef: BookRef) = bookPath(bookRef).joinPath(CONTENT)
        fun mediaPath(bookRef: BookRef) = contentPath(bookRef).joinPath(MEDIA)
        fun pagesPath(bookRef: BookRef) = contentPath(bookRef).joinPath(PAGES)
        fun configPath(bookRef: BookRef) = bookPath(bookRef).joinPath(CONFIG_JSON)
        fun logicPath(bookRef: BookRef) = contentPath(bookRef).joinPath(LOGIC_JSON)
        fun coverPath(bookRef: BookRef, coverName: String) = bookPath(bookRef).joinPath(coverName)
        fun pagePath(bookRef: BookRef, pageId: Long) = pagesPath(bookRef).joinPath(pageJson(pageId))
        fun pageImagePath(bookRef: BookRef, imageName: String) = mediaPath(bookRef).joinPath(imageName)

        private fun String.joinPath(other: String) = "$this/$other"
    }
}

fun File.bookFile(bookRef: BookRef) = File(this, BookPath.bookPath(bookRef))
fun File.contentFile(bookRef: BookRef) = File(this, BookPath.contentPath(bookRef))
fun File.mediaFile(bookRef: BookRef) = File(this, BookPath.mediaPath(bookRef))
fun File.pagesFile(bookRef: BookRef) = File(this, BookPath.pagesPath(bookRef))
fun File.configFile(bookRef: BookRef) = File(this, BookPath.configPath(bookRef))
fun File.logicFile(bookRef: BookRef) = File(this, BookPath.logicPath(bookRef))
fun File.coverFile(bookRef: BookRef, coverName: String) = File(this,
    BookPath.coverPath(bookRef, coverName)
)
fun File.pageFile(bookRef: BookRef, pageId: Long) = File(this,
    BookPath.pagePath(bookRef, pageId)
)
fun File.pageImageFile(bookRef: BookRef, imageName: String) = File(this,
    BookPath.pageImagePath(bookRef, imageName)
)