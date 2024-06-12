package com.fancymansion.core.presentation.window

data class TypeWindow(
    val pane : TypePane,
    val orientation : TypeOrientation
)

enum class TypePane {
    SINGLE,
    DUAL
}

enum class TypeOrientation {
    PORTRAIT,
    LANDSCAPE,
    LANDSCAPE_WIDE
}