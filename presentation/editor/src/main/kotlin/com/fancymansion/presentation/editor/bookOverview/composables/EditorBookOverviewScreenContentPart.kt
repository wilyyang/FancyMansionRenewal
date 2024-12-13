package com.fancymansion.presentation.editor.bookOverview.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract
import com.fancymansion.presentation.editor.bookOverview.KeywordState
import com.fancymansion.presentation.editor.bookOverview.PageBrief

val itemMarginHeight = 15.dp

@Composable
fun EditOverviewCoverImage(
    modifier : Modifier = Modifier,
    imagePickType: ImagePickType,
    onClickGalleryCoverPick: () -> Unit,
    onClickCoverImageReset: () -> Unit
){
    val painter = when(imagePickType){
        is ImagePickType.SavedImage ->{
            rememberAsyncImagePainter(imagePickType.file)
        }
        is ImagePickType.GalleryUri ->{
            rememberAsyncImagePainter(imagePickType.uri)
        }
        else -> {
            painterResource(id = R.drawable.ic_gallery_photo)
        }
    }

    Column(modifier = modifier) {

        Spacer(modifier = Modifier.height(itemMarginHeight))

        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_overview_top_label_book_cover)
        )

        Box{
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                    .size(72.dp)
                    .clip(shape = MaterialTheme.shapes.small)
                    .border(
                        0.5.dp,
                        color = onSurfaceSub,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickSingle {
                        onClickGalleryCoverPick()
                    },
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "Gallery"
            )

            if (imagePickType != ImagePickType.Empty) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(25.dp)
                        .clickSingle {
                            onClickCoverImageReset()
                        },
                    painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_text_cancel),
                    contentDescription = "Delete"
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun EditOverviewTitle(
    modifier : Modifier = Modifier,
    title: String,
    updateBookInfoTitle: (String) -> Unit
){
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_overview_top_label_book_title)
        )

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Paddings.Basic.vertical
                ),
            value = title,
            maxLine = 2,
            hint = stringResource(id = R.string.edit_overview_edit_hint_book_title),
            isCancelable = true
        ) {
            updateBookInfoTitle(it)
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditOverviewKeyword(
    modifier : Modifier = Modifier,
    keywordStates : List<KeywordState>,
    onOpenEditKeywords: () -> Unit
){
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(vertical = Paddings.Basic.vertical)
                .fillMaxWidth()
        ) {
            CommonEditInfoTitle(
                title = stringResource(id = R.string.edit_overview_top_label_book_keyword)
            )

            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickSingle {
                        onOpenEditKeywords()
                    },
                text = stringResource(id = R.string.edit_overview_top_edit_keyword),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            keywordStates.filter { it.selected.value }.forEach { keywordState ->
                Text(
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .clip(shape = MaterialTheme.shapes.extraSmall)
                        .padding(0.5.dp)
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(horizontal = 7.dp, vertical = 5.dp),
                    text = "#${keywordState.keyword.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@Composable
fun EditOverviewPageList(
    modifier : Modifier = Modifier,
    pageBriefList : List<PageBrief>,
    onPageListMoreClicked: () -> Unit,
    onPageListEditModeClicked: () -> Unit,
    onPageContentButtonClicked: (Long) -> Unit
){
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(vertical = Paddings.Basic.vertical)
                .fillMaxWidth()
        ) {
            CommonEditInfoTitle(
                title = stringResource(id = R.string.edit_overview_top_label_book_page)
            )

            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickSingle {
                        onPageListEditModeClicked()
                    },
                text = stringResource(id = R.string.edit_overview_top_edit_keyword),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }

        pageBriefList.take(5).forEach {
            PageBriefHolder(
                pageBrief = it,
                onPageContentButtonClicked = onPageContentButtonClicked
            )
        }

        Text(
            modifier = Modifier
                .padding(12.dp)
                .clickSingle {
                    onPageListMoreClicked()
                },
            text = stringResource(id = R.string.edit_overview_button_page_more)
        )

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@Composable
fun PageBriefHolder(
    pageBrief: PageBrief,
    onPageContentButtonClicked : (Long) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .borderLine(
                density = LocalDensity.current,
                color = MaterialTheme.colorScheme.outline,
                top = 0.5.dp,
                bottom = 0.5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.9f)) {
            Text(
                text = pageBrief.title,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "page : ${pageBrief.id}",
                style = MaterialTheme.typography.labelMedium
            )
        }

        Text(
            modifier = Modifier.clickSingle {
                onPageContentButtonClicked(pageBrief.id)
            },
            text = stringResource(id = R.string.edit_overview_button_holder_page_edit),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun EditOverviewDescription(
    modifier : Modifier = Modifier,
    description : String,
    updateBookInfoDescription: (String) -> Unit
){
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_overview_top_label_book_introduce)
        )

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Paddings.Basic.vertical
                ), value = description,
            minLine = 15,
            maxLine = 15,
            hint = stringResource(id = R.string.edit_overview_edit_hint_book_introduce)
        ) {
            updateBookInfoDescription(it)
        }
    }
}

@Composable
fun CommonEditInfoTitle(
    modifier: Modifier = Modifier,
    verticalPadding : Dp = Paddings.Basic.vertical,
    title: String,
    style : TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
){
    Text(
        modifier = modifier.padding(vertical = verticalPadding),
        text = title,
        style = style
    )
}