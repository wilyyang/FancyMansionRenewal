package com.fancymansion.presentation.editor.bookOverview.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.PublishStatus
import com.fancymansion.core.common.util.formatTimestampDateTime
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.RoundedRectangleShape
import com.fancymansion.core.presentation.compose.theme.ColorSet
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract
import com.fancymansion.presentation.editor.bookOverview.KeywordState
import com.fancymansion.presentation.editor.bookOverview.PageBrief
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.itemMarginHeight

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
fun EditOverviewPublishSection(
    modifier: Modifier = Modifier,
    isPublished: Boolean,
    metadata: BookMetaModel,
    onClickUploadBook: () -> Unit,
    onClickUpdateBook: () -> Unit,
    onClickWithdrawBook: () -> Unit
) {
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_overview_top_label_book_publish_section)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.edit_overview_publish_section_status_label))
            Spacer(Modifier.width(6.dp))
            Text(text = stringResource(metadata.status.resId))
        }

        Spacer(Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.edit_overview_publish_section_update_at_label))
            Spacer(Modifier.width(6.dp))
            Text(text = if(metadata.status == PublishStatus.PUBLISHED){
                formatTimestampDateTime(metadata.updatedAt)
            }else{
                "-"
            })
        }

        Spacer(Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.edit_overview_publish_section_version_label))
            Spacer(Modifier.width(6.dp))
            Text(text = "${if (isPublished) metadata.version else "-"}")
        }

        Spacer(Modifier.height(10.dp))

        if (isPublished) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PublishSectionButton(
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    buttonText = stringResource(R.string.edit_overview_button_publish_update),
                    onClickEvent = { onClickUpdateBook() }
                )

                Spacer(modifier = Modifier.width(15.dp))

                PublishSectionButton(
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    buttonText = stringResource(R.string.edit_overview_button_publish_withdraw),
                    onClickEvent = { onClickWithdrawBook() }
                )
            }
        } else {
            PublishSectionButton(
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                buttonText = stringResource(R.string.edit_overview_button_publish_upload),
                onClickEvent = { onClickUploadBook() }
            )
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@Composable
fun PublishSectionButton(
    backgroundColor: Color,
    textColor: Color,
    buttonText: String,
    onClickEvent: () -> Unit
) {
    Text(
        modifier = Modifier
            .width(130.dp)
            .clip(shape = RoundedRectangleShape())
            .background(backgroundColor)
            .padding(vertical = 12.dp)
            .clickSingle { onClickEvent() },
        text = buttonText,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        color = textColor
    )
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

        Spacer(modifier = Modifier.height(itemMarginHeight * 2))
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
                .padding(vertical = Paddings.Basic.vertical, horizontal = Paddings.Basic.horizontal)
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
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)

        val limitedList = pageBriefList.take(5)
        limitedList.forEachIndexed { index, pageBrief ->
            PageBriefHolder(
                pageBrief = pageBrief,
                onPageContentButtonClicked = onPageContentButtonClicked
            )

            if (index < limitedList.size - 1) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    thickness = 0.3.dp,
                    color = onSurfaceSub
                )
            }
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickSingle(
                indication = LocalIndication.current
            ) {
                onPageListMoreClicked()
            }
            .padding(vertical = 13.dp)) {

            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.edit_overview_button_page_more, pageBriefList.size),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    modifier = Modifier.height(25.dp),
                    painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_right_w300),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "More"
                )
            }
        }


        Spacer(modifier = Modifier.height(itemMarginHeight * 2))
    }
}

@Composable
fun PageBriefHolder(
    pageBrief: PageBrief,
    onPageContentButtonClicked : (Long) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clickSingle(
                indication = LocalIndication.current
            ) {
                onPageContentButtonClicked(pageBrief.id)
            }
            .padding(horizontal = 18.dp)
    ) {
        Column(modifier = Modifier
            .align(Alignment.CenterStart)
            .fillMaxWidth(0.75f)) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(2.dp))
                        .background(
                            color = when (pageBrief.type) {
                                PageType.START -> ColorSet.cyan_1ecdcd
                                PageType.ENDING -> ColorSet.navy_324155
                                else -> ColorSet.blue_1e9eff
                            }
                        )
                        .padding(horizontal = 3.dp, vertical = 1.dp)

                ) {
                    Text(
                        text = stringResource(id = pageBrief.type.localizedName.resId),
                        style = MaterialTheme.typography.labelMedium,
                        color = ColorSet.white_ffffff
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = pageBrief.title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }


            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.edit_overview_page_holder_page_number, pageBrief.id),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(3.dp))
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(shape = MaterialTheme.shapes.large)
                .padding(0.5.dp)
                .border(
                    width = 0.5.dp,
                    color = onSurfaceSub,
                    shape = MaterialTheme.shapes.large
                )
                .padding(horizontal = 6.dp, vertical = 4.dp),
            text = stringResource(id = R.string.edit_overview_page_holder_selector_count, pageBrief.selectorCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
            minLine = 8,
            maxLine = 15,
            hint = stringResource(id = R.string.edit_overview_edit_hint_book_introduce)
        ) {
            updateBookInfoDescription(it)
        }
    }
}