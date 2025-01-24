package com.fancymansion.presentation.editor.pageContent.composables.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.pageContent.SourceWrapper

const val detailPanelCornerHeight = 12
val detailPanelShape = RoundedCornerShape(topStart = detailPanelCornerHeight.dp, topEnd = detailPanelCornerHeight.dp)

@Composable
fun CommonEditPageContentBottomDialog(
    bottomDialogType: String,
    source: SourceWrapper? = null,
    updateSourceText: (String) -> Unit,
    onClickImagePick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(detailPanelShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = Paddings.Basic.horizontal)
            .padding(top = 10.dp)
            .clickSingle { }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                CommonEditInfoTitle(
                    modifier = Modifier
                        .padding(vertical = Paddings.Basic.vertical)
                        .fillMaxWidth(),
                    title = "텍스트 수정"
                )

                if(bottomDialogType == "text"){
                    RoundedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = Paddings.Basic.vertical
                            ),
                        value = (source as SourceWrapper.TextWrapper).description,
                        hint = stringResource(id = R.string.edit_page_content_edit_hint_page_title),
                        isCancelable = true
                    ) {
                        updateSourceText(it)
                    }
                }else if(bottomDialogType == "image"){
                    val imagePickType = (source as SourceWrapper.ImageWrapper).imagePickType
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
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .size(72.dp)
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
                        contentScale = ContentScale.Crop,
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