package com.fancymansion.core.presentation.base

import android.os.SystemClock
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

class OnSingleClickListener(
    private val interval : Int = 600,
    var onSingleClick: () -> Unit
) : ()->Unit {

    private var lastClickTime: Long = 0

    override fun invoke() {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if ((elapsedRealtime - lastClickTime) < interval) {
            return
        }
        lastClickTime = elapsedRealtime
        onSingleClick()
    }
}
fun Modifier.clickSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    clickInterval: Int = 600,
    indication : Indication? = null,
    interactionSource : MutableInteractionSource? = null,
    onClick: () -> Unit
) = composed {

    val rememberOnClick = remember { OnSingleClickListener(clickInterval, onClick) }.apply {
        this.onSingleClick = onClick
    }

    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { rememberOnClick() },
        role = role,
        indication = indication,
        interactionSource = interactionSource?: remember { MutableInteractionSource() }
    )
}