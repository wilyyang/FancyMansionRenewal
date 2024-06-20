package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.presentation.viewer.content.ContentData
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
    val contentList : List<ContentData> = listOf(
        ContentData.Text("어쩌고 저쩌고 라이라이 차차차"),
        ContentData.Image(R.drawable.img_small_color_sky_btn),
        ContentData.Text("아나고는 맛있당 이이이이리리리도도 ㄷ튜튜튜"),
        ContentData.Image(R.drawable.img_small_color_sky_btn),
        ContentData.Image(R.drawable.img_small_color_sky_btn),
        ContentData.Text("뇬릳ㅈ=머ㅏㅣㅇㄴ머리ㅏㅁㄴㄹㄷ"),
    )

    LazyColumn(modifier = Modifier.background(color = ColorSet.white_ffffff)) {
        items(contentList){
            when(it){
                is ContentData.Text -> {
                    Text(text = it.text)

                }
                is ContentData.Image -> {
                    Image(painter = painterResource(id = it.url), contentDescription = null)
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