package com.fancymansion.core.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.core.presentation.theme.dimmedAlpha
import com.fancymansion.core.presentation.R

@Composable
fun ErrorDialog(
    title : String? = null,
    message : String? = null,
    errorMessage : String? = null,
    confirmText : String = "",
    dismissText : String? = "",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val isShowDialog = remember{ mutableStateOf(true) }
    ScreenPopup()
    {
        if(isShowDialog.value){
            val height = LocalConfiguration.current.screenHeightDp.dp
            Box (
                modifier = Modifier
                    .semantics {
                        contentDescription = "ErrorDialog"
                    }
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = dimmedAlpha)),
                contentAlignment = Alignment.Center
            ){
                Column(modifier = Modifier
                    .width(280.dp)
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 0.dp, max = height * 0.6f)
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(Color.White)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorSet.yellow_ffcb00)
                            .padding(horizontal = 5.dp)
                            .padding(top = 10.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_error_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = title?:stringResource(id = com.fancymansion.core.common.R.string.alarm),
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 17.sp),
                            color = Color.White,
                            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(top = 15.dp)
                                .heightIn(min = 45.dp),
                        ) {
                            Text(
                                text = message?:stringResource(id = com.fancymansion.core.common.R.string.dialog_error),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                            )

                            errorMessage?.let {
                                Text(
                                    text = errorMessage,
                                    style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
                                    color = ColorSet.gray_bcbcbc,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.End
                        ){
                            dismissText?.let {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                        .clip(shape = MaterialTheme.shapes.small)
                                        .clickable {
                                            onDismiss()
                                            isShowDialog.value = false
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(15.dp),
                                        text = it.ifBlank { stringResource(id = com.fancymansion.core.common.R.string.cancel) },
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = ColorSet.gray_5f5f5f
                                    )
                                }
                            }
                            Box (
                                modifier = Modifier
                                    .clip(shape = MaterialTheme.shapes.small)
                                    .clickable {
                                        onConfirm()
                                        isShowDialog.value = false
                                    }
                                    .padding(end = 5.dp)
                            ){
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(15.dp),
                                    text = confirmText.ifBlank { stringResource(id = com.fancymansion.core.common.R.string.confirm) },
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = ColorSet.yellow_ffcb00
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ErrorDialogPreview(
) {
    FancyMansionTheme {
        ErrorDialog(
            title  = "값이 없음",
            message = "예상치 못한 오류 발생",
            errorMessage  = "API ERROR CODE : 404",
            onConfirm = {},
            onDismiss = {}
        )
    }
}