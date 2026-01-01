package com.fancymansion.core.presentation.base.screen

interface ScreenResult

data class EditorBookOverviewResult(
    val isEditSaved: Boolean
) : ScreenResult