package com.fancymansion.test.fake.bookStorage.data

import android.content.Context
import com.fancymansion.data.datasource.appStorage.book.model.BookInfoData
import com.fancymansion.data.datasource.appStorage.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import com.fancymansion.test.R
import com.fancymansion.test.common.readRaw
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FakeBookStorageData(private val context: Context) {

    private val typeBookInfo : Type = object : TypeToken<BookInfoData>(){}.type
    fun getBookInfo_base() : BookInfoData = Gson()
        .fromJson(context.readRaw(R.raw.base_book_info), typeBookInfo)

    private val typeEpisodeInfo : Type = object : TypeToken<EpisodeInfoData>(){}.type
    fun getEpisodeInfo_base() : EpisodeInfoData = Gson()
        .fromJson(context.readRaw(R.raw.base_episode_info), typeEpisodeInfo)

    private val typeLogicInfo : Type = object : TypeToken<LogicData>(){}.type
    fun getLogic_base() : LogicData = Gson()
        .fromJson(context.readRaw(R.raw.base_logic), typeLogicInfo)


    private val typePage : Type = object : TypeToken<PageData>() {}.type
    fun getPage_base(pageId: Long): PageData {
        return context.run {
            val resourceId = resources.getIdentifier("base_page_$pageId", "raw", packageName)
            Gson().fromJson(readRaw(resourceId), typePage)
        }
    }
}