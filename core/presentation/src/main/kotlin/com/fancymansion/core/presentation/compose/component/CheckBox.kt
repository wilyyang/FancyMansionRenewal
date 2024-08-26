package com.fancymansion.core.presentation.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.ColorSet

@Composable
fun TextCheckBox(
    modifier : Modifier = Modifier,
    checkBoxSize : Dp,
    checked : Boolean = true,
    checkBackgroundColor : Color = MaterialTheme.colorScheme.primary,
    checkBorderColor : Color = MaterialTheme.colorScheme.outline,

    checkboxText : String = "",
    textColor : Color = ColorSet.gray_333333,
    textStartPadding : Dp = 6.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,

    isMatchTop : Boolean = false,

    onClickChecked : (Boolean) -> Unit = {}
) {
    val textPadding = if (isMatchTop) {
        Modifier
    } else {
        if(checkBoxSize < textStyle.lineHeight.value.dp){
            Modifier
        }else{
            Modifier.padding(top = (checkBoxSize - textStyle.lineHeight.value.dp) / 2)
        }
    }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier.align(Alignment.TopStart)
        ){
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart),
                text = "",
                color = Color.White,
                style = textStyle
            )

            BasicCheckBox(
                modifier = Modifier
                    .align(if (isMatchTop) Alignment.TopStart else Center),
                checkBoxSize = checkBoxSize,
                checked = checked,
                checkBackgroundColor = checkBackgroundColor,
                checkBorderColor= checkBorderColor,
                onClickChecked = onClickChecked
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .then(textPadding)
                .padding(start = checkBoxSize + textStartPadding)
                .clickSingle {
                    onClickChecked(!checked)
                },
            text = checkboxText,
            color = textColor,
            style = textStyle
        )
    }
}

@Composable
fun BasicCheckBox(
    modifier : Modifier = Modifier,
    checkBoxSize: Dp,
    checked : Boolean = true,
    checkBackgroundColor : Color = MaterialTheme.colorScheme.primary,
    checkBorderColor : Color = MaterialTheme.colorScheme.outline,
    onClickChecked : (Boolean) -> Unit = {}
) {
    Card(
        modifier = modifier
            .size(checkBoxSize)
            .background(Color.Transparent),
        shape = MaterialTheme.shapes.small.copy(CornerSize(checkBoxSize/5)),
        border = if(checked) null else BorderStroke(1.dp, color = checkBorderColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (checked) checkBackgroundColor else Color.White)
                .clickSingle {
                    onClickChecked(!checked)
                },
            contentAlignment = Center
        ) {
            if(checked)
                Icon(
                    modifier = Modifier.fillMaxSize(0.7f),
                    painter = painterResource(id = R.drawable.img_check_rounded),
                    contentDescription = "",
                    tint = Color.White
                )
        }
    }
}

@Preview
@Composable
fun TextCheckBoxPreview(){
    Column (modifier = Modifier.background(color = Color.White)){

        TextCheckBox(
            modifier = Modifier.padding(10.dp).background(MaterialTheme.colorScheme.background),
            checkBoxSize = 40.dp,
            checkboxText = "힌트힌트힌트"
        )
        TextCheckBox(
            modifier = Modifier.padding(10.dp).background(MaterialTheme.colorScheme.background),
            checkBoxSize = 40.dp,
            checkboxText = "힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트"
        )
        TextCheckBox(
            modifier = Modifier.padding(10.dp).height(100.dp).background(MaterialTheme.colorScheme.background),
            checkBoxSize = 40.dp,
            checkboxText = "힌트힌트힌트",
            textStyle = MaterialTheme.typography.bodySmall
        )

        TextCheckBox(
            modifier = Modifier.padding(10.dp).height(100.dp).background(MaterialTheme.colorScheme.background),
            checkBoxSize = 40.dp,
            isMatchTop = true,
            checkboxText = "힌트힌트힌트",
            textStyle = MaterialTheme.typography.bodySmall
        )

        TextCheckBox(
            modifier = Modifier.padding(10.dp).height(100.dp).background(MaterialTheme.colorScheme.background),
            checkBoxSize = 10.dp,
            checkboxText = "힌트힌트힌트",
            textStyle = MaterialTheme.typography.titleLarge
        )

        TextCheckBox(
            modifier = Modifier.padding(10.dp).height(100.dp).background(MaterialTheme.colorScheme.background),
            checkBoxSize = 10.dp,
            checkboxText = "힌트힌트힌트",
            isMatchTop = true,
            textStyle = MaterialTheme.typography.titleLarge
        )

    }
}