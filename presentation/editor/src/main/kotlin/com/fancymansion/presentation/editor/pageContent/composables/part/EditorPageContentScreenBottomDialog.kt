package com.fancymansion.presentation.editor.pageContent.composables.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.customImePadding
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.pageContent.SourceWrapper

const val detailPanelCornerHeight = 12
val detailPanelShape = RoundedCornerShape(topStart = detailPanelCornerHeight.dp, topEnd = detailPanelCornerHeight.dp)

@Composable
fun CommonEditPageContentBottomDialog(
    source: SourceWrapper? = null,
    onClickDeleteSource: () -> Unit,
    updateSourceText: (String) -> Unit,
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
                            .height(216.dp)
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