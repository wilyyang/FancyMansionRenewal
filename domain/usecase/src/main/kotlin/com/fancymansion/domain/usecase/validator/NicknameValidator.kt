package com.fancymansion.domain.usecase.validator

import com.fancymansion.domain.model.user.result.NicknameUpdateResult

object NicknameValidator {
    fun validate(nickname: String): NicknameUpdateResult {
        val trimmed = nickname.trim()

        if (trimmed.isEmpty()) {
            return NicknameUpdateResult.Invalid.Empty
        }

        if (trimmed.length !in 2..8) {
            return NicknameUpdateResult.Invalid.LengthInvalid
        }

        val regex = Regex("^[가-힣a-zA-Z0-9]+$")
        if (!regex.matches(trimmed)) {
            return NicknameUpdateResult.Invalid.InvalidCharacter
        }

        if (Regex("^[0-9]+$").matches(trimmed)) {
            return NicknameUpdateResult.Invalid.OnlyNumber
        }

        return NicknameUpdateResult.Valid
    }
}