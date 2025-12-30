package com.fancymansion.core.common.const

import java.io.File

const val testUserId = "tester"
const val sampleUserId = "sample"
fun getBookId(userId: String, mode: ReadMode, number: Int) = "${userId}_${mode.name}_${number}"
fun getEpisodeId(userId: String, mode: ReadMode, number: Int) = "${userId}_${mode.name}_${number}.0"
fun getEpisodeId(bookId: String) = "${bookId}.0"

fun getCoverFileName(id: String, number: Int, fileExtension: String) = "${id}.cover.${number}.$fileExtension"
fun getPageImageFileName(pageNumber: Long, imageNumber: Int) = "page.${pageNumber}.image.${imageNumber}"

const val pageStartWith = "page."
private val pageImageFileRegex = Regex("""^page\.(\d+)\.image\.(\d+)\.[^.]+$""")
private fun parsePageAndImageNumber(fileName: String): Pair<Long, Int>? {
    val m = pageImageFileRegex.matchEntire(fileName) ?: return null
    val page = m.groupValues[1].toLongOrNull() ?: return null
    val image = m.groupValues[2].toIntOrNull() ?: return null
    return page to image
}

fun nextPageImageNumber(images: List<File>, pageNumber: Long): Int {
    val usedNumbers = images
        .mapNotNull { file -> parsePageAndImageNumber(file.name) }
        .filter { (page, _) -> page == pageNumber }
        .map { (_, image) -> image }
        .toHashSet()

    var next = 0
    while (usedNumbers.contains(next)) next++
    return next
}

val sampleEpisodeRef = EpisodeRef(
    userId = sampleUserId,
    mode = ReadMode.EDIT,
    bookId = getBookId(sampleUserId, ReadMode.EDIT, 31),
    episodeId = getEpisodeId(sampleUserId, ReadMode.EDIT, 31)
)