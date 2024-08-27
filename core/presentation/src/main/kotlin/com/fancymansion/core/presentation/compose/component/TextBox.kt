package com.fancymansion.core.presentation.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.compose.modifier.clickSingle

@Composable
fun RoundedTextField(
    modifier : Modifier = Modifier,
    @androidx.annotation.IntRange(from = 1)
    minLine : Int = 1,
    maxLine : Int = 1,

    value : String,
    textStyle : TextStyle = MaterialTheme.typography.bodyLarge,
    hint : String = "",

    imeAction : ImeAction = ImeAction.Default,
    keyboardType : KeyboardType = KeyboardType.Text,
    keyboardActions : KeyboardActions = KeyboardActions.Default,

    rememberFocus : MutableState<Boolean> = remember { mutableStateOf(false) },
    focusRequester : FocusRequester = remember { FocusRequester() },

    borderShape : Shape = MaterialTheme.shapes.small,
    textPadding : Dp = 13.5.dp,

    isEnabled : Boolean = true,
    onValueChange : (String) -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = if (isEnabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (rememberFocus.value) {
        MaterialTheme.colorScheme.outline
    } else {
        backgroundColor
    }


    val passwordVisible = rememberSaveable { mutableStateOf(keyboardType != KeyboardType.Password) }
    val visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()

    Row(
        // 보더 영역
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = borderShape
            )
            .padding(0.5.dp)
            .clip(
                shape = borderShape
            )
            .background(backgroundColor).padding(start = textPadding, end = 8.dp)
            .focusRequester(focusRequester = focusRequester),

        verticalAlignment = Alignment.CenterVertically) {

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
                    Text(text = hint,  maxLines = maxLine, style = textStyle.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
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
                    R.drawable.ic_text_password_hide
                } else {
                    R.drawable.ic_text_password_show
                }),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )

        }else if(value.isNotEmpty()){
            Image(
                modifier = Modifier.padding(start = 6.dp, end = 3.dp).height(textStyle.lineHeight.value.dp + textPadding/2).clickSingle {
                    focusRequester.requestFocus()
                    onValueChange("")
                },
                painter = painterResource(id = R.drawable.ic_text_cancel),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )
        }

    }
}

@Preview
@Composable
fun RoundedTextFieldPreview(){
    Column (modifier = Modifier.background(color = Color.Red)){
        RoundedTextField(
            modifier = Modifier.padding(10.dp),
            value = "",
            hint = "힌트힌트힌트",
            isEnabled  = true,
            onValueChange = {}
        )

        RoundedTextField(
            modifier = Modifier.padding(10.dp),
            value = "입력된 텍스트",
            hint = "힌트힌트힌트",
            isEnabled  = true,
            onValueChange = {}
        )

        RoundedTextField(
            modifier = Modifier.padding(10.dp),
            value = "",
            hint = "텍스트 입력 불가",
            isEnabled  = false,
            onValueChange = {}
        )
    }

}