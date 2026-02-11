package com.fancymansion.presentation.editor.bookOverview.composables.part

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.util.formatTimestampDateTime
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.presentation.editor.R

@Composable
fun EditOverviewPublishSection(
    modifier: Modifier = Modifier,
    isPublishInfoExpanded: Boolean,
    isPublished: Boolean,
    metadata: BookMetaModel,
    onClickHeader: () -> Unit,
    onClickUploadBook: () -> Unit,
    onClickUpdateBook: () -> Unit,
    onClickWithdrawBook: () -> Unit
) {
    Column(modifier = modifier.padding(vertical = 6.dp)) {

        PublishSectionHeader(
            isExpanded = isPublishInfoExpanded,
            statusText = stringResource(metadata.status.resId),
            onClick = onClickHeader
        )

        if (isPublishInfoExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            PublishInfoRow(
                labelResId = R.string.edit_overview_publish_section_status_label,
                value = stringResource(metadata.status.resId)
            )
            PublishInfoRow(
                labelResId = R.string.edit_overview_publish_section_update_at_label,
                value = if (isPublished) formatTimestampDateTime(metadata.updatedAt) else "-"
            )
            PublishInfoRow(
                labelResId = R.string.edit_overview_publish_section_version_label,
                value = if (isPublished) metadata.version.toString() else "-"
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isPublished) {
                PublishSectionButton(
                    buttonText = stringResource(R.string.edit_overview_button_publish_update),
                    onClick = onClickUpdateBook
                )

                Spacer(modifier = Modifier.height(8.dp))

                WithdrawHint(onClickWithdrawBook = onClickWithdrawBook)
            } else {
                PublishSectionButton(
                    buttonText = stringResource(R.string.edit_overview_button_publish_upload),
                    onClick = onClickUploadBook
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun PublishSectionHeader(
    isExpanded: Boolean,
    statusText: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Paddings.Basic.vertical)
            .clickable(
                interactionSource = interactionSource,
                indication = null, onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val headerText = if (isExpanded) {
            stringResource(id = R.string.edit_overview_top_label_book_publish_header)
        } else {
            stringResource(id = R.string.edit_overview_top_label_book_publish_header_status, statusText)
        }

        Text(
            modifier = Modifier.weight(1f),
            text = headerText,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        Icon(
            modifier = Modifier.height(22.dp),
            painter = painterResource(
                id = if (isExpanded)
                    R.drawable.ic_header_up_96dp_w400
                else
                    R.drawable.ic_header_down_96dp_w400
            ),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = if (isExpanded) "Collapse" else "Expand"
        )
    }
}

@Composable
private fun PublishInfoRow(
    @StringRes labelResId: Int,
    value: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(labelResId))
        Spacer(Modifier.width(6.dp))
        Text(text = value)
    }
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
private fun WithdrawHint(
    onClickWithdrawBook: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.edit_overview_publish_header_guide_withdraw),
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            modifier = Modifier
                .padding(start = 6.dp)
                .clickSingle(onClick = onClickWithdrawBook),
            text = stringResource(R.string.edit_overview_button_publish_withdraw),
            style = MaterialTheme.typography.bodySmall.copy(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun PublishSectionButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier
            .width(100.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 8.dp)
            .clickSingle(onClick = onClick)
            .semantics { role = Role.Button },
        text = buttonText,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}