package com.fancymansion.presentation.main.tab.my.composables.part

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.EditorTabContract

@Composable
fun EditNicknameDialog(
    text: String = "",
    hint: String = "",
    warningMessage: String = "",
    textInput: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val isEnabled = text.isNotBlank() && warningMessage.isEmpty()
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.8f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.White)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
                    .padding(horizontal = 25.dp, vertical = 30.dp)
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = stringResource(R.string.my_edit_nickname),
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundedTextField(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        value = text,
                        hint = hint,
                        maxLine = 1,
                        imeAction = ImeAction.Default,
                        keyboardActions = KeyboardActions {
                            if (isEnabled) {
                                onConfirm()
                            }
                        }
                    ) {
                        textInput(it)
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .padding(0.5.dp)
                            .border(
                                0.5.dp,
                                MaterialTheme.colorScheme.onSurface.copy(
                                    alpha =
                                        if (isEnabled) 0.35f else 0.15f
                                ),
                                MaterialTheme.shapes.extraSmall
                            )
                            .clickSingle(
                                enabled = isEnabled
                            ) {
                                onConfirm()
                            }
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        text = stringResource(R.string.my_edit_button_update_nickname),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha =
                                if (isEnabled) 0.7f else 0.35f
                        )
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = warningMessage,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}