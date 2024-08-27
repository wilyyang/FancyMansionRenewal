package com.fancymansion.core.presentation.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.theme.ColorSet

@Composable
fun TextWithDot(
    modifier: Modifier = Modifier,
    dotSize: Dp,
    dotColor : Color = ColorSet.gray_a1b0c5,
    text: AnnotatedString,
    textColor : Color = ColorSet.gray_333333,
    textStartPadding : Dp = 6.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
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

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color = dotColor)
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = dotSize + textStartPadding),
            text = text,
            color = textColor,
            style = textStyle
        )
    }
}

@Composable
fun TextWithDot(
    modifier: Modifier = Modifier,

    dotSize: Dp,
    dotColor : Color = ColorSet.gray_a1b0c5,

    text: String,
    textColor : Color = ColorSet.gray_333333,
    textStartPadding : Dp = 6.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
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

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color = dotColor)
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = dotSize + textStartPadding),
            text = text,
            color = textColor,
            style = textStyle
        )
    }
}

@Preview
@Composable
fun TextWithDotPreview(){
    Column (modifier = Modifier.background(color = Color.White)){

        TextWithDot(
            modifier = Modifier.padding(10.dp),
            dotSize = 3.dp,
            dotColor = Color.Red,
            text = "힌트힌트힌트"
        )
        TextWithDot(
            modifier = Modifier.padding(10.dp),
            dotSize = 3.dp,
            dotColor = Color.Red,
            text = "힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트 힌트힌트힌트"
        )

        TextWithDot(
            modifier = Modifier.padding(10.dp),
            dotSize = 5.dp,
            dotColor = Color.Black,
            text = "힌트힌트힌트",
            textStyle = MaterialTheme.typography.labelSmall
        )

        TextWithDot(
            modifier = Modifier.padding(10.dp),
            dotSize = 7.dp,
            text = "힌트힌트힌트",
            textStartPadding = 2.dp,
            textStyle = MaterialTheme.typography.headlineLarge
        )
    }
}