package com.fancymansion.domain.model.homeBook.result

import com.fancymansion.domain.model.homeBook.HomeBookItemModel

sealed class LoadBookResult {
    data class Success(val model: HomeBookItemModel) : LoadBookResult()
    object Withdrawn : LoadBookResult()
    object NotFound : LoadBookResult()
}