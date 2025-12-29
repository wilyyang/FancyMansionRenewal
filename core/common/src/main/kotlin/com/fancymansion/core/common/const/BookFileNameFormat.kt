package com.fancymansion.core.common.const

const val testUserId = "tester"
const val sampleUserId = "sample"

const val imageFileNamePrefix = "_page_img_"
fun getBookId(userId: String, mode: ReadMode, number: Int) = "${userId}_${mode.name}_${number}"
fun getEpisodeId(userId: String, mode: ReadMode, number: Int) = "${userId}_${mode.name}_${number}.0"
fun getEpisodeId(bookId: String) = "${bookId}.0"

fun baseBookCoverName(id: String, number: Int, fileExtension: String) = "cover_${id}_${number}.$fileExtension"

val sampleEpisodeRef = EpisodeRef(
    userId = sampleUserId,
    mode = ReadMode.EDIT,
    bookId = getBookId(sampleUserId, ReadMode.EDIT, 31),
    episodeId = getEpisodeId(sampleUserId, ReadMode.EDIT, 31)
)