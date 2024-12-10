package com.fancymansion.presentation.editor.bookOverview.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.bookOverview.KeywordState
import com.fancymansion.presentation.editor.bookOverview.PageBrief

val EDIT_ITEM_VERTICAL_PADDING = 5.dp
val EDIT_ITEM_HORIZONTAL_PADDING = 10.dp

@Composable
fun EditOverviewTopInfo(
    modifier : Modifier = Modifier,
    imagePickType: ImagePickType,
    title: String,
    onClickGalleryCoverPick: () -> Unit,
    onClickCoverImageReset: () -> Unit,
    updateBookInfoTitle: (String) -> Unit
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

        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_overview_top_label_book_cover)
        )

        Box(modifier = Modifier.size(74.dp)) {
            Image(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .clip(shape = MaterialTheme.shapes.small)
                    .border(
                        0.5.dp,
                        color = onSurfaceSub,
                        shape = MaterialTheme.shapes.medium
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
                        .size(20.dp)
                        .clickSingle {
                            onClickCoverImageReset()
                        },
                    painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_text_cancel),
                    contentDescription = "Delete"
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_overview_top_label_book_title)
        )

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = EDIT_ITEM_VERTICAL_PADDING
                ),
            value = title,
            maxLine = 2,
            hint = stringResource(id = R.string.edit_overview_edit_hint_book_title),
            isCancelable = true
        ) {
            updateBookInfoTitle(it)
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun EditOverviewKeyword(
    modifier : Modifier = Modifier,
    keywordStates : List<KeywordState>,
    onOpenEditKeywords: () -> Unit
){
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(
                    vertical = EDIT_ITEM_VERTICAL_PADDING,
                    horizontal = EDIT_ITEM_HORIZONTAL_PADDING
                )
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
                style = MaterialTheme.typography.labelLarge
            )
        }


        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = EDIT_ITEM_VERTICAL_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {

            item {
                Spacer(modifier = Modifier.width(EDIT_ITEM_HORIZONTAL_PADDING))
            }

            items(keywordStates.filter { it.selected.value }) { keywordState ->
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

            item {
                Spacer(modifier = Modifier.width(EDIT_ITEM_HORIZONTAL_PADDING))
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
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
                .padding(
                    vertical = EDIT_ITEM_VERTICAL_PADDING
                )
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
                text = stringResource(id = R.string.edit_overview_top_edit_page),
                style = MaterialTheme.typography.labelLarge
            )
        }

        val density = LocalDensity.current
        pageBriefList.take(5).forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .borderLine(
                        density = density,
                        color = MaterialTheme.colorScheme.outline,
                        top = 0.5.dp,
                        bottom = 0.5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.bodySmall
                    )


                    Text(
                        text = "page : ${it.id}",
                        style = MaterialTheme.typography.labelMedium
                    )

                }

                Text(
                    modifier = Modifier.clickSingle {
                        onPageContentButtonClicked(it.id)
                    },
                    text = stringResource(id = R.string.edit_overview_button_holder_page_edit),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(12.dp)
                .clickSingle {
                    onPageListMoreClicked()
                },
            text = stringResource(id = R.string.edit_overview_button_page_more)
        )

        Spacer(modifier = Modifier.height(15.dp))
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
                    vertical = EDIT_ITEM_VERTICAL_PADDING
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
    title: String,
    style : TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
){
    Text(
        modifier = Modifier.padding(
            vertical = EDIT_ITEM_VERTICAL_PADDING
        ),
        text = title,
        style = style
    )
}