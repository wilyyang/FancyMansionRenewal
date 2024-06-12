package com.fancymansion.core.common.util

import java.util.regex.Pattern

fun checkNameValidation(name: String): Boolean {
    if (name.isBlank()) return false

    val nameRegex = Regex("^[가-힣]{2,8}$|^[a-zA-Z]{4,16}$")
    val nameKorAndEngTest = Pattern.matches(nameRegex.pattern, name)

    val specialCharacterRegex = Regex(".*[^A-Za-zㄱ-ㅎㅏ-ㅣ가-힣].*")
    val specialCharacterTest = Pattern.matches(specialCharacterRegex.pattern, name)

    return nameKorAndEngTest && !specialCharacterTest
}

fun checkNameWriteValidation(name: String): Boolean {
    val nameRegex = Regex("^[ㄱ-ㅎ가-힣]{0,8}$|^[a-zA-Z]{0,16}$")
    val nameKorAndEngTest = Pattern.matches(nameRegex.pattern, name)

    val specialCharacterRegex = Regex(".*[^A-Za-zㄱ-ㅎㅏ-ㅣ가-힣].*")
    val specialCharacterTest = Pattern.matches(specialCharacterRegex.pattern, name)

    return nameKorAndEngTest && !specialCharacterTest
}

fun checkEmailValidation(name: String) = Pattern.matches(Regex("([A-Z0-9a-z._-])+@[A-Z0-9a-z.-]+\\.[A-Za-z]{2,50}").pattern, name)
fun checkPasswordValidation(name: String) = Pattern.matches(Regex(".{4,50}").pattern, name)