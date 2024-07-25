package com.fancymansion.core.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC

@Composable
fun NoDataScreen(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Outlined.Info,
    titleMessage: String = "데이터가 없습니다.",
    detailMessage: String = "네트워크 연결 확인 후 재시도 해주세요.",
    option1Title: String? = null,
    option2Title: String? = null,
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {}
) {
    val buttonHorizontalPadding = 10.dp
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "No Data",
            modifier = Modifier.size(64.dp)
        )

        Text(
            text = titleMessage,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = detailMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        // 옵션 버튼
        Spacer(modifier = Modifier.height(16.dp))

        Row (modifier = Modifier.width(240.dp)){
            if (option1Title != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(horizontal = buttonHorizontalPadding)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(0.5.dp)
                        .clip(
                            shape = MaterialTheme.shapes.large
                        ).clickable {
                            onClickOption1()
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = option1Title,
                    textAlign = TextAlign.Center
                )
            }

            if (option2Title != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = buttonHorizontalPadding)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(0.5.dp)
                        .clip(
                            shape = MaterialTheme.shapes.large
                        ).clickable {
                            onClickOption2()
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = option2Title,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun NoDataScreenPreview(){
    NoDataScreen(
        modifier = Modifier.fillMaxSize().background(color = Color.White),
        option1Title = "재시도",
        option2Title = "취소"
    )
}