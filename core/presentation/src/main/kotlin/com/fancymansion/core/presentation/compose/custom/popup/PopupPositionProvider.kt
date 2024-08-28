package com.fancymansion.core.presentation.compose.custom.popup

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

/**
 * <custom>
 * AlignmentOffsetPositionProvider
 *
 *         val anchorAlignmentPoint = alignment.align(
 *             IntSize.Zero,
 *             anchorBounds.size,
 *             layoutDirection
 *         )
 *         // Note the negative sign. Popup alignment point contributes negative offset.
 *         val popupAlignmentPoint = -alignment.align(
 *             IntSize.Zero,
 *             popupContentSize,
 *             layoutDirection
 *         )
 *         val resolvedUserOffset = IntOffset(
 *             offset.x * (if (layoutDirection == LayoutDirection.Ltr) 1 else -1),
 *             offset.y
 *         )
 *
 *         return anchorBounds.topLeft +
 *             anchorAlignmentPoint +
 *             popupAlignmentPoint +
 *             resolvedUserOffset
 */
internal class AlignmentOffsetPositionProvider(
    val alignment: Alignment,
    val offset: IntOffset
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // TODO: Decide which is the best way to round to result without reimplementing Alignment.align
        var popupPosition = IntOffset(0, 0)

        // Get the aligned point inside the parent
        val parentAlignmentPoint = alignment.align(
            IntSize.Zero,
            IntSize(anchorBounds.width, anchorBounds.height),
            layoutDirection
        )
        // Get the aligned point inside the child
        val relativePopupPos = alignment.align(
            IntSize.Zero,
            IntSize(popupContentSize.width, popupContentSize.height),
            layoutDirection
        )

        // Add the position of the parent
        popupPosition += IntOffset(anchorBounds.left, anchorBounds.top)

        // Add the distance between the parent's top left corner and the alignment point
        popupPosition += parentAlignmentPoint

        // Subtract the distance between the children's top left corner and the alignment point
        popupPosition -= IntOffset(relativePopupPos.x, relativePopupPos.y)

        // Add the user offset
        val resolvedOffset = IntOffset(
            offset.x * (if (layoutDirection == LayoutDirection.Ltr) 1 else -1),
            offset.y
        )
        popupPosition += resolvedOffset

        return popupPosition
    }
}
