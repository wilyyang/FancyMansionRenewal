package com.fancymansion.core.presentation.dialog

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.core.presentation.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.theme.onSurfaceSub
import com.fancymansion.core.presentation.util.borderLine

@Composable
fun AlarmDialog(
    title : String? = null,
    message : String? = null,
    background : Color? = null,
    confirmText : String? = "",
    dismissText : String? = "",
    onConfirm: () -> Unit = { },
    onDismiss: () -> Unit = { }
) {
    val isShowDialog = remember{ mutableStateOf(true) }
    ScreenPopup()
    {
        if(isShowDialog.value){
            val height = LocalConfiguration.current.screenHeightDp.dp
            Box (
                modifier = Modifier
                    .semantics {
                        contentDescription = "AlarmDialog"
                    }
                    .fillMaxSize()
                    .background(color = background ?: Color.Transparent),
                contentAlignment = Alignment.Center
            ){
                Column(modifier = Modifier
                    .width(300.dp)
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 0.dp, max = height * 0.6f)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(Color.White)
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp).padding(top = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(title != null){
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Box (modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 5.dp)){
                        Text(
                            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                                .padding(horizontal = 25.dp).padding(vertical = 15.dp),
                            text = message ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(color = onSurfaceDimmed, lineHeight = 22.sp),
                            textAlign = TextAlign.Center
                        )

                        Box(modifier = Modifier.fillMaxWidth().height(15.dp).align(Alignment.TopStart).background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color.Transparent
                                )
                            )
                        ))

                        Box(modifier = Modifier.fillMaxWidth().height(15.dp).align(Alignment.BottomStart).background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                )
                            )
                        ))
                    }

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
                        dismissText?.let {
                            Box(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clickable {
                                        onDismiss()
                                        isShowDialog.value = false
                                    }
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center).padding(20.dp),
                                    text = it.ifBlank { stringResource(id = com.fancymansion.core.common.R.string.cancel) },
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = onSurfaceSub
                                )
                            }
                        }

                        if(dismissText != null && confirmText != null){
                            VerticalDivider(modifier = Modifier.width(1.dp), color = MaterialTheme.colorScheme.outline)
                        }

                        confirmText?.let {
                            Box (
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clickable {
                                        onConfirm()
                                        isShowDialog.value = false
                                    }
                            ){
                                Text(
                                    modifier = Modifier.align(Alignment.Center).padding(20.dp),
                                    text = confirmText.ifBlank { stringResource(id = com.fancymansion.core.common.R.string.confirm) },
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

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun AlarmDialogPreview(
) {
    FancyMansionTheme {
        AlarmDialog(
            title = "알림",
            message = "로그아웃 하시겠습니까?",
            onConfirm = {},
            onDismiss = {}
        )
    }
}