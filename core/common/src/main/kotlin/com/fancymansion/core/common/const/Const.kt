package com.fancymansion.core.common.const

val testBookTitle = "배트의 어드벤처"
val testEpisodeTitle = "1화"
val testEpisodeRef = EpisodeRef(
    userId = "test_user_id",
    mode = ReadMode.READ,
    bookId = "test_book_id",
    episodeId = "test_book_id_0"
)

const val MOBILE_BASE_SCREEN_HEIGHT_PX = 2340
const val MOBILE_BASE_SCREEN_WIDTH_PX = 1080
const val MOBILE_BASE_SCREEN_DPI = 450
const val MOBILE_BASE_SCREEN_DENSITY = MOBILE_BASE_SCREEN_DPI / 160f // 2.8125f

const val TABLET_BASE_SCREEN_HEIGHT_PX = 1600
const val TABLET_BASE_SCREEN_WIDTH_PX = 2560
const val TABLET_BASE_SCREEN_DPI = 340
const val TABLET_BASE_SCREEN_DENSITY = TABLET_BASE_SCREEN_DPI / 160f // 2.125f

const val BASE_URL = "https://test.com"

const val DURATION_SHORTER              = 150L
const val DURATION_SHORT                = 300L
const val DURATION_NORMAL               = 500L
const val DURATION_LONG                 = 1000L
const val DURATION_LONGER               = 1500L

enum class NetworkState{
    TYPE_NOT_CONNECTED,
    TYPE_MOBILE,
    TYPE_WIFI,
    TYPE_ETC
}

const val STATUS_SUCCESS               = 200

const val DELAY_LOADING_SHOW_MS = 500L
const val DELAY_LOADING_FADE_OUT_MS = 800L
const val ANIMATION_LOADING_FADE_OUT_MS = 700