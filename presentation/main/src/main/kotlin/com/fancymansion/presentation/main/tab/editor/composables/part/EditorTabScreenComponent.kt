package com.fancymansion.presentation.main.tab.editor.composables.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.backgroundLight
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.compose.theme.onSurfaceInactive
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.main.R


@Composable
fun SearchTextField(
    modifier : Modifier = Modifier,
    @androidx.annotation.IntRange(from = 1)
    minLine : Int = 1,
    maxLine : Int = Int.MAX_VALUE,

    value : String,
    textStyle : TextStyle = MaterialTheme.typography.bodyLarge,
    hint : String = "",

    imeAction : ImeAction = ImeAction.Search,
    keyboardType : KeyboardType = KeyboardType.Text,
    keyboardActions : KeyboardActions = KeyboardActions.Default,

    rememberFocus : MutableState<Boolean> = remember { mutableStateOf(false) },
    focusRequester : FocusRequester = remember { FocusRequester() },

    borderShape : Shape = MaterialTheme.shapes.small,
    textPadding : Dp = 13.5.dp,

    isCancelable : Boolean = false,
    isEnabled : Boolean = true,
    onValueChange : (String) -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onBackground
    val backgroundColor = if(isEnabled) backgroundLight else MaterialTheme.colorScheme.background

    val passwordVisible = rememberSaveable { mutableStateOf(keyboardType != KeyboardType.Password) }
    val visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()

    Row(
        // 보더 영역
        modifier = modifier
            .clip(
                shape = borderShape
            )
            .background(backgroundColor).padding(start = textPadding, end = 8.dp)
            .focusRequester(focusRequester = focusRequester),

        verticalAlignment = Alignment.CenterVertically) {

        Image(
            modifier = Modifier.padding(end = 5.dp).height(textStyle.lineHeight.value.dp + textPadding/2),
            painter = painterResource(id = R.drawable.ic_search_w300),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter.tint(onSurfaceDimmed)
        )

        // 입력 영역
        BasicTextField(
            modifier = Modifier.weight(1f)
                .padding(vertical = textPadding)
                .onFocusChanged {rememberFocus.value = it.isFocused },
            enabled = isEnabled,

            singleLine = maxLine == 1,
            minLines = minLine,
            maxLines = maxLine,
            value = value,
            textStyle = textStyle.copy(color = textColor),

            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->

                // 힌트 영역
                if (value.isEmpty() && hint.isNotEmpty()) {
                    Text(text = hint,  maxLines = maxLine, style = textStyle.copy(color = if(isEnabled) onSurfaceSub else onSurfaceInactive))
                }
                innerTextField()
            },
            onValueChange = onValueChange,
        )

        if (keyboardType == KeyboardType.Password) {
            Image(
                modifier = Modifier.padding(start = 6.dp, end = 3.dp).height(textStyle.lineHeight.value.dp + textPadding/2).clickSingle {
                    focusRequester.requestFocus()
                    passwordVisible.value = !passwordVisible.value
                },
                painter = painterResource(id = if (passwordVisible.value) {
                    com.fancymansion.core.presentation.R.drawable.ic_text_password_hide
                } else {
                    com.fancymansion.core.presentation.R.drawable.ic_text_password_show
                }),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )

        }else if(isCancelable && value.isNotEmpty()){
            Image(
                modifier = Modifier.padding(start = 6.dp, end = 3.dp).height(textStyle.lineHeight.value.dp + textPadding/2).clickSingle {
                    focusRequester.requestFocus()
                    onValueChange("")
                },
                painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_text_cancel),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )
        }

    }
}