package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.Source

data class Content(
    val pages: List<Page>
)

data class Page(
    val id: Long,
    val title: String,
    val sources: List<Source>
)