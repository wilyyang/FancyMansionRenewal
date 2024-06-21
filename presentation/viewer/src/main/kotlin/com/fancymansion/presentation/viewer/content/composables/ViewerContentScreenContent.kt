package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.domain.model.book.Source
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.content

@Composable
fun ViewerContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    ViewerContentScreenPageContent()
}


@Composable
fun ViewerContentScreenPageContent(){

    LazyColumn {
        items(content.pages[0].sources){
            when(it){
                is Source.Text -> {
                    Text(text = it.description)
                }
                is Source.Image -> {
                    Image(painter = painterResource(id = it.resId), contentDescription = "")
                }
            }
        }
    }

}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ViewerContentScreenPageContentPreview(){
    FancyMansionTheme {
        ViewerContentScreenPageContent()
    }
}