package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.domain.model.book.Source
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.content
import com.fancymansion.presentation.viewer.content.logic

@Composable
fun ViewerContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    ViewerContentScreenPageContent()
}


@Composable
fun ViewerContentScreenPageContent() {

    val idx = 7
    LazyColumn {
        item {
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = "[" +logic.logics[idx].type + "] "+content.pages[idx].title, style = MaterialTheme.typography.headlineLarge
            )
        }
        items(content.pages[idx].sources) {
            when (it) {
                is Source.Text -> {
                    Text(text = it.description)
                }

                is Source.Image -> {
                    Image(painter = painterResource(id = it.resId), contentDescription = "")
                }
            }

        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(logic.logics[idx].selectors) {
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .background(color = ColorSet.sky_c1ebfe)
                    .clickable {
                        Logger.e("click ${it.id} ${it.text}")
                    }
                    .padding(5.dp)
            ) {
                Text(text = it.text)
            }
        }
    }

}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ViewerContentScreenPageContentPreview() {
    FancyMansionTheme {
        ViewerContentScreenPageContent()
    }
}