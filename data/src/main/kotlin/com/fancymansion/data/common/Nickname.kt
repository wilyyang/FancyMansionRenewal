package com.fancymansion.data.common

const val NICKNAME_NOT_INIT = "NOT_INIT"
private val initialNicknameWords = listOf(
    "비글", "고양이", "토끼", "여우", "판다",
    "햄스터", "호랑이", "곰", "사자", "다람쥐"
)

fun generateNickname(): String {
    val word = initialNicknameWords.random()

    val suffix = buildString {
        repeat(2) { append(('A'..'Z').random()) }
        repeat(3) { append((0..9).random()) }
    }

    return "$word-$suffix" // 예: 비글-AB123
}

class NicknameDuplicateException : Exception()