package com.fancymansion.presentation.main.tab.my.composables.part

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.presentation.main.R
import kotlin.text.indexOf

@Composable
fun UserNickname(
    modifier: Modifier = Modifier,
    nickname: String,
    email: String,
    onEditClick: () -> Unit
) {

    Column(
        modifier = modifier
    ) {
        val formattedNickname = formatNickname(nickname = nickname)
        Text(
            text = formattedNickname,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodySmall,
            color = onSurfaceDimmed
        )

        Row(
            modifier = Modifier
                .height(32.dp)
                .clickSingle {
                    onEditClick()
                }
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.my_edit_nickname),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = onSurfaceDimmed
            )

            Icon(
                modifier = Modifier
                    .padding(top = 1.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_right_w300),
                tint = onSurfaceDimmed,
                contentDescription = "Edit Nickname"
            )
        }
    }
}

@Composable
fun formatNickname(nickname: String): AnnotatedString {
    val raw = stringResource(R.string.my_user_nickname_text, nickname)

    return buildAnnotatedString {
        append(raw)

        val start = raw.indexOf(nickname)
        val end = start + nickname.length

        addStyle(
            SpanStyle(fontWeight = FontWeight.Bold),
            start,
            end
        )
    }
}