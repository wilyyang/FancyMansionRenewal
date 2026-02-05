package com.fancymansion.domain.model.book

data class LocalBookItemModel(
    val book: BookInfoModel,
    val metaData: BookMetaModel,
    val episode: EpisodeInfoModel
)