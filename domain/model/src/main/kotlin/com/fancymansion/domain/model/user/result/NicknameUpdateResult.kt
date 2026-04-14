package com.fancymansion.domain.model.user.result

sealed interface NicknameUpdateResult {
    data object Unknown : NicknameUpdateResult
    data object Valid : NicknameUpdateResult
    data object Success : NicknameUpdateResult
    sealed interface Invalid : NicknameUpdateResult {
        data object Empty : Invalid
        data object LengthInvalid : Invalid
        data object InvalidCharacter : Invalid
        data object OnlyNumber : Invalid
        data object Duplicate : Invalid
    }
}