package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.presentation.viewer.content.ViewerContentContract

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

}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ViewerContentScreenPageContentPreview(){
    FancyMansionTheme {
        ViewerContentScreenPageContent()
    }
}