package com.fancymansion.core.common.const

import android.net.Uri
import java.io.File

const val MOBILE_BASE_SCREEN_HEIGHT_PX = 2340
const val MOBILE_BASE_SCREEN_WIDTH_PX = 1080
const val MOBILE_BASE_SCREEN_DPI = 450
const val MOBILE_BASE_SCREEN_DENSITY = MOBILE_BASE_SCREEN_DPI / 160f // 2.8125f

const val TABLET_BASE_SCREEN_HEIGHT_PX = 1600
const val TABLET_BASE_SCREEN_WIDTH_PX = 2560
const val TABLET_BASE_SCREEN_DPI = 340
const val TABLET_BASE_SCREEN_DENSITY = TABLET_BASE_SCREEN_DPI / 160f // 2.125f

const val BASE_URL = "https://test.com"

const val DEFAULT_PROCESSING_DELAY_TIME = 15000L

const val DURATION_SHORTER              = 150L
const val DURATION_SHORT                = 300L
const val DURATION_NORMAL               = 500L
const val DURATION_LONG                 = 1000L
const val DURATION_LONGER               = 1500L

enum class NetworkState{
    TYPE_NOT_CONNECTED,
    TYPE_MOBILE_DATA,
    TYPE_WIFI,
    TYPE_ETC
}

sealed class ImagePickType {
    data object Empty : ImagePickType()
    class SavedImage(val file: File) : ImagePickType()
    class GalleryUri(val uri: Uri) : ImagePickType()
}

const val STATUS_SUCCESS               = 200

const val ANIM_DURATION_VERTICAL_ENTER = 300
const val ANIM_DURATION_VERTICAL_EXIT = 300
const val ANIM_DURATION_HORIZONTAL_ENTER = 300
const val ANIM_DURATION_HORIZONTAL_EXIT = 300

const val DELAY_SCREEN_ANIMATION_MS = 330L
const val DELAY_LOADING_SHOW_MS = 300L
const val DELAY_LOADING_FADE_OUT_MS = 800L
const val ANIMATION_LOADING_FADE_OUT_MS = 700
const val DELAY_CLICK_SINGLE_MS = 600