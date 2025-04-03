package com.fancymansion.presentation.editor.common

import androidx.compose.ui.unit.dp
import com.fancymansion.domain.model.book.ConditionModel

val itemMarginHeight = 15.dp


data class ConditionState(var editIndex : Int, val condition : ConditionModel)