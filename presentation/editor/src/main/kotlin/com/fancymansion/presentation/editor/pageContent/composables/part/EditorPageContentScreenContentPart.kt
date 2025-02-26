package com.fancymansion.presentation.editor.pageContent.composables.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.itemMarginHeight

@Composable
fun PageContentHeader(
    modifier: Modifier = Modifier,
    contentSourceSize : Int,
    onShowSelectorList : () -> Unit,
    onAddContentSourceClicked : () -> Unit
){
    val itemPaddingEnd = 12.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .borderLine(density = LocalDensity.current, color = onSurfaceSub, bottom = 0.3.dp)
            .padding(
                vertical = Paddings.Basic.vertical,
                horizontal = Paddings.Basic.horizontal
            )
    ) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_page_content_header_source_size, contentSourceSize)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            Text(
                modifier = Modifier
                    .padding(end = itemPaddingEnd)
                    .clickSingle {
                        onShowSelectorList()
                    },
                text = stringResource(id = R.string.edit_page_content_header_item_show_selector),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                modifier = Modifier
                    .clickSingle {
                        onAddContentSourceClicked()
                    },
                text = stringResource(id = R.string.edit_page_content_header_item_add_content_source),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun EditPageTitle(
    modifier : Modifier = Modifier,
    title: String,
    updateBookInfoTitle: (String) -> Unit
){
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_page_content_top_label_page_title)
        )

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Paddings.Basic.vertical
                ),
            value = title,
            maxLine = 2,
            hint = stringResource(id = R.string.edit_page_content_edit_hint_page_title),
            isCancelable = true
        ) {
            updateBookInfoTitle(it)
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@Composable
fun EditPageSourceText(
    modifier : Modifier = Modifier,
    text: String,
    onClickText: () -> Unit
){
    Text(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .padding(0.5.dp)
            .clip(
                shape = MaterialTheme.shapes.small
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickSingle {
                onClickText()
            }
            .padding(13.5.dp),
        text = text,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun EditPageSourceImage(
    modifier : Modifier = Modifier,
    imagePickType: ImagePickType,
    onClickGalleryImagePick: () -> Unit
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

    Image(
        modifier = modifier
            .padding(vertical = 10.dp)
            .size(72.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .border(
                0.5.dp,
                color = onSurfaceSub,
                shape = MaterialTheme.shapes.small
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .clickSingle {
                onClickGalleryImagePick()
            },
        painter = painter,
        contentScale = ContentScale.Crop,
        contentDescription = "Gallery"
    )
}

@Composable
fun PageTypeDropdown(
    selectedType: PageType,
    onItemSelected: (PageType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(
            vertical = Paddings.Basic.vertical
        )
    ) {
        Text(
            text = stringResource(id = selectedType.localizedName.resId),
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(0.5.dp)
                .clip(
                    shape = MaterialTheme.shapes.small
                )
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
        )

        // 드롭다운 메뉴
        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(x = 0.dp, y = (-30).dp),
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = true),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(0.5.dp)
                .clip(
                    shape = MaterialTheme.shapes.small
                )
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            PageType.entries.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = type.localizedName.resId),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (type == selectedType) FontWeight.Bold else FontWeight.Normal)
                        )
                    },
                    onClick = {
                        onItemSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}