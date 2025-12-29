package com.fancymansion.data.datasource.appStorage.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import java.io.File

class BookPath {
    companion object {
        private const val BOOKS = "books"
        private const val EPISODES = "episodes"
        private const val CONTENT = "content"
        private const val MEDIA = "media"
        private const val PAGES = "pages"
        private const val BOOK_INFO_JSON = "book.info.json"
        private const val EPISODE_INFO_JSON = "episode.info.json"
        private const val LOGIC_JSON = "logic.json"

        private fun pageJson(pageId: Long) = "$pageId.json"

        fun booksPath() = BOOKS
        fun userPath(userId: String) = booksPath().joinPath(userId)
        fun modePath(userId: String, mode: ReadMode) = userPath(userId).joinPath(mode.name)
        fun bookPath(userId: String, mode: ReadMode, bookId: String) = modePath(userId, mode).joinPath(bookId)
        fun episodePath(episodeRef: EpisodeRef) = bookPath(episodeRef.userId, episodeRef.mode, episodeRef.bookId).joinPath(EPISODES).joinPath(episodeRef.episodeId)
        fun contentPath(episodeRef: EpisodeRef) = episodePath(episodeRef).joinPath(CONTENT)
        fun mediaPath(episodeRef: EpisodeRef) = contentPath(episodeRef).joinPath(MEDIA)
        fun pagesPath(episodeRef: EpisodeRef) = contentPath(episodeRef).joinPath(PAGES)
        fun bookInfoPath(userId: String, mode: ReadMode, bookId: String) = bookPath(userId, mode, bookId).joinPath(BOOK_INFO_JSON)
        fun episodeInfoPath(episodeRef: EpisodeRef) = episodePath(episodeRef).joinPath(EPISODE_INFO_JSON)
        fun logicPath(episodeRef: EpisodeRef) = contentPath(episodeRef).joinPath(LOGIC_JSON)
        fun coverPath(userId: String, mode: ReadMode, bookId: String, imageName: String) = bookPath(userId, mode, bookId).joinPath(imageName)
        fun episodeThumbnailPath(episodeRef: EpisodeRef, imageName: String) = episodePath(episodeRef).joinPath(imageName)
        fun pagePath(episodeRef: EpisodeRef, pageId: Long) = pagesPath(episodeRef).joinPath(pageJson(pageId))
        fun pageImagePath(episodeRef: EpisodeRef, imageName: String) = mediaPath(episodeRef).joinPath(imageName)

        private fun String.joinPath(other: String) = "$this/$other"
    }
}

fun File.bookFile(userId: String, mode: ReadMode, bookId: String) = File(this, BookPath.bookPath(userId, mode, bookId))
fun File.episodeFile(episodeRef: EpisodeRef) = File(this, BookPath.episodePath(episodeRef))
fun File.contentFile(episodeRef: EpisodeRef) = File(this, BookPath.contentPath(episodeRef))
fun File.mediaFile(episodeRef: EpisodeRef) = File(this, BookPath.mediaPath(episodeRef))
fun File.pagesFile(episodeRef: EpisodeRef) = File(this, BookPath.pagesPath(episodeRef))
fun File.bookInfoFile(userId: String, mode: ReadMode, bookId: String) = File(this, BookPath.bookInfoPath(userId, mode, bookId))
fun File.episodeInfoFile(episodeRef: EpisodeRef) = File(this, BookPath.episodeInfoPath(episodeRef))

fun File.logicFile(episodeRef: EpisodeRef) = File(this, BookPath.logicPath(episodeRef))
fun File.coverFile(userId: String, mode: ReadMode, bookId: String, imageName: String) = File(this,
    BookPath.coverPath(userId, mode, bookId, imageName)
)
fun File.episodeThumbnailFile(episodeRef: EpisodeRef, imageName: String) = File(this,
    BookPath.episodeThumbnailPath(episodeRef, imageName)
)

fun File.pageFile(episodeRef: EpisodeRef, pageId: Long) = File(this,
    BookPath.pagePath(episodeRef, pageId)
)
fun File.pageImageFile(episodeRef: EpisodeRef, imageName: String) = File(this,
    BookPath.pageImagePath(episodeRef, imageName)
)