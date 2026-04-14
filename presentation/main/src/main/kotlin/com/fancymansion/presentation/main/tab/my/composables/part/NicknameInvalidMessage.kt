package com.fancymansion.presentation.main.tab.my.composables.part

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fancymansion.domain.model.user.result.NicknameUpdateResult
import com.fancymansion.presentation.main.R

@Composable
fun NicknameUpdateResult.Invalid.toMessage(): String {
    return when (this) {
        NicknameUpdateResult.Invalid.Duplicate -> stringResource(R.string.nickname_result_already_exists)
        NicknameUpdateResult.Invalid.Empty -> stringResource(R.string.nickname_result_empty)
        NicknameUpdateResult.Invalid.InvalidCharacter -> stringResource(R.string.nickname_result_invalid_character)
        NicknameUpdateResult.Invalid.LengthInvalid -> stringResource(R.string.nickname_result_length_invalid)
        NicknameUpdateResult.Invalid.OnlyNumber -> stringResource(R.string.nickname_result_only_number)
    }
}