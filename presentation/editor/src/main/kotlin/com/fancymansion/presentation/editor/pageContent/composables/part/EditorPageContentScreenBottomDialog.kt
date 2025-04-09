package com.fancymansion.presentation.editor.pageContent.composables.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.customImePadding
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.pageContent.SourceWrapper

const val detailPanelCornerHeight = 12
val detailPanelShape = RoundedCornerShape(topStart = detailPanelCornerHeight.dp, topEnd = detailPanelCornerHeight.dp)

@Composable
fun SelectSourceDialog(
    onTextSelected: () -> Unit,
    onImageSelected: () -> Unit,
    onCanceled: () -> Unit
) {
    val isShowDialog = remember{ mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
    ) {
        Popup()
        {
            if(isShowDialog.value){
                val height = LocalConfiguration.current.screenHeightDp.dp
                Box (
                    modifier = Modifier
                        .semantics {
                            contentDescription = "CustomDialog"
                        }
                        .fillMaxSize()
                        .clickSingle { onCanceled() },
                    contentAlignment = Alignment.Center
                ){
                    Column(modifier = Modifier
                        .width(300.dp)
                        .height(IntrinsicSize.Min)
                        .heightIn(min = 0.dp, max = height * 0.6f)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .border(
                            0.5.dp,
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.medium
                        )
                        .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 25.dp)
                                .padding(vertical = 40.dp),
                            text = stringResource(id = R.string.edit_page_content_add_source_dialog),
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface, lineHeight = 24.sp),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .borderLine(
                                    density = LocalDensity.current,
                                    color = MaterialTheme.colorScheme.outline,
                                    top = 1.dp
                                )
                        ){
                            Box(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clickable {
                                        onTextSelected()
                                        isShowDialog.value = false
                                    }
                            ) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(20.dp),
                                    text = stringResource(id = R.string.edit_page_content_add_source_dialog_button_text),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            VerticalDivider(modifier = Modifier.width(1.dp), color = MaterialTheme.colorScheme.outline)

                            Box (
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clickable {
                                        onImageSelected()
                                        isShowDialog.value = false
                                    }
                            ){
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(20.dp),
                                    text = stringResource(id = R.string.edit_page_content_add_source_dialog_button_image),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommonEditPageContentBottomDialog(
    source: SourceWrapper? = null,
    onClickDeleteSource: () -> Unit,
    updateSourceText: (TextFieldValue) -> Unit,
    onClickImagePick: () -> Unit
) {
    Box(
        modifier = Modifier
            .customImePadding()
            .fillMaxWidth()
            .clip(detailPanelShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = Paddings.Basic.horizontal)
            .padding(top = 10.dp)
            .clickSingle { }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .padding(vertical = Paddings.Basic.vertical)
                        .fillMaxWidth()
                ) {
                    CommonEditInfoTitle(
                        title = stringResource(
                            id = if (source is SourceWrapper.TextWrapper) R.string.edit_page_content_edit_source_text
                            else R.string.edit_page_content_edit_source_image
                        )
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickSingle {
                                onClickDeleteSource()
                            },
                        text = stringResource(id = R.string.edit_page_content_edit_source_delete),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }

                if(source is SourceWrapper.TextWrapper){
                    RoundedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = Paddings.Basic.vertical
                            ),
                        value = source.description.value,
                        hint = stringResource(id = R.string.edit_page_content_edit_source_text_hint),
                        isCancelable = true
                    ) {
                        updateSourceText(it)
                    }
                }else if(source is SourceWrapper.ImageWrapper){
                    val painter = when(val imagePickType = source.imagePickType){
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
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .height(if(source.imagePickType is ImagePickType.Empty) 72.dp else 216.dp)
                            .clip(shape = MaterialTheme.shapes.small)
                            .border(
                                0.5.dp,
                                color = onSurfaceSub,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickSingle {
                                onClickImagePick()
                            },
                        painter = painter,
                        contentScale = ContentScale.FillHeight,
                        contentDescription = "Gallery"
                    )
                }

            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}