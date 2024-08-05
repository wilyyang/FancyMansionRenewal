package com.fancymansion.core.common.const

import com.fancymansion.core.common.R

val testEpisodeRef = EpisodeRef(
    userId = "test_user_id",
    mode = ReadMode.READ,
    bookId = "test_book_id",
    episodeId = "test_book_id_0"
)

const val MOBILE_BASE_SCREEN_HEIGHT_PX = 1920
const val MOBILE_BASE_SCREEN_WIDTH_PX = 1080
const val MOBILE_BASE_SCREEN_DPI = 480
const val MOBILE_BASE_SCREEN_DENSITY = MOBILE_BASE_SCREEN_DPI / 160f // 3.0f

const val TABLET_BASE_SCREEN_HEIGHT_PX = 1200
const val TABLET_BASE_SCREEN_WIDTH_PX = 1920
const val TABLET_BASE_SCREEN_DPI = 320
const val TABLET_BASE_SCREEN_DENSITY = TABLET_BASE_SCREEN_DPI / 160f // 2.0f

const val BASE_URL = "https://test.com"

const val NOT_LOGIN_USER_ID = "NOT_LOGIN_USER_ID"
const val INIT_CAMPAIGN_ID = 0

const val DURATION_SHORTEST             = 100L
const val DURATION_SHORTER              = 150L
const val DURATION_VIDEO_SPEED          = 250L
const val DURATION_SHORT                = 300L
const val DURATION_MENU_ANIMATION_PHONE = 350L
const val DURATION_NORMAL               = 500L
const val DURATION_CHANGE_USER          = 600L
const val DURATION_SHORT_LONG           = 700L
const val DURATION_LONG                 = 1000L
const val DURATION_LONGER               = 1500L
const val DURATION_LONGEST              = 2000L
const val DURATION_EASTER_EGG           = 5000L
const val DURATION_GAME_LOAD_ERROR      = 30000L

enum class NetworkState{
    TYPE_NOT_CONNECTED,
    TYPE_MOBILE,
    TYPE_WIFI,
    TYPE_ETC
}

enum class FindAccountType(val webParam : String, val nameResId : Int){
    None(webParam = "", nameResId = R.string.find_account_type_none),
    ID(webParam = "id", nameResId = R.string.find_account_type_id),
    Password(webParam = "password", nameResId = R.string.find_account_type_password)
}

object ArgName{
    const val NAME_USER_ID = "NAME_USER_ID"
    const val NAME_READ_MODE = "NAME_READ_MODE"
    const val NAME_BOOK_ID = "NAME_BOOK_ID"
    const val NAME_EPISODE_ID = "NAME_EPISODE_ID"
}

object CurrentDensity {
    var density : Float? = null
}

const val STATUS_NOT_DEFINED           = -1
const val STATUS_SUCCESS               = 200
const val STATUS_LOGIN_INVALID_ID_PW   = 403
const val STATUS_LOGIN_LIMITED_SERVICE = 406

const val DELAY_LOADING_SHOW_MS = 500L
const val DELAY_LOADING_FADE_OUT_MS = 800L
const val ANIMATION_LOADING_FADE_OUT_MS = 700