package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED

data class ActionIdModel(
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED
)