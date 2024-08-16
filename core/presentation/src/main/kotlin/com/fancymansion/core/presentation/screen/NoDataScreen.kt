package com.fancymansion.core.presentation.screen

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.theme.CONTENT_DIMMED_ALPHA
import com.fancymansion.core.presentation.theme.FancyMansionTheme

@Composable
fun NoDataScreen(
    modifier: Modifier = Modifier,
    imageResId: Int = R.drawable.ic_unknown_page,
    titleMessage: StringValue = StringValue.StringResource(R.string.screen_no_data_title_default),
    detailMessage: StringValue = StringValue.StringResource(R.string.screen_no_data_detail_default),
    option1Title: StringValue = StringValue.Empty,
    option2Title: StringValue = StringValue.Empty,
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = imageResId),
            contentDescription = "No Data",
            modifier = Modifier.size(54.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = CONTENT_DIMMED_ALPHA)
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = titleMessage.asString(LocalContext.current),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = CONTENT_DIMMED_ALPHA)),
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = detailMessage.asString(LocalContext.current),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = CONTENT_DIMMED_ALPHA)),
            textAlign = TextAlign.Center
        )

        Column (modifier = Modifier.padding(top = 15.dp, bottom = 30.dp).width(200.dp)){
            if (option1Title != StringValue.Empty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(0.5.dp)
                        .clip(
                            shape = MaterialTheme.shapes.large
                        )
                        .clickSingle(
                            indication = LocalIndication.current
                        ) {
                            onClickOption1()
                        }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    text = option1Title.asString(LocalContext.current),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            if (option2Title != StringValue.Empty) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(0.5.dp)
                        .clip(
                            shape = MaterialTheme.shapes.large
                        )
                        .clickSingle(
                            indication = LocalIndication.current
                        ) {
                            onClickOption2()
                        }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    text = option2Title.asString(LocalContext.current),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun NoDataScreenPreview(){
    FancyMansionTheme {
        NoDataScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            option1Title = StringValue.StringResource(R.string.button_retry),
            option2Title = StringValue.StringResource(R.string.button_cancel)
        )
    }
}