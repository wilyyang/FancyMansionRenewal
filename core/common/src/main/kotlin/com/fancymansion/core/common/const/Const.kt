package com.fancymansion.core.common.const

import com.fancymansion.core.common.R

val testBookRef = BookRef(
    userId = "test_user_id",
    mode = ReadMode.EDIT,
    bookId = "test_book_id"
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
    const val nameCurriculumId = "nameCurriculumId"
    const val nameFindAccountType = "nameFindAccountType"
    const val nameToken = "nameToken"
    const val nameArticleId = "nameArticleId"
    const val nameLectureId = "nameLectureId"
    const val nameCourseId = "nameCourseId"
    const val nameTextBookId = "nameTextBookId"
    const val nameUrl = "nameUrl"
    const val nameGameSeparator = "nameGameSeparator"
    const val nameUserId = "nameUserId"
    const val nameUserName = "nameUserName"
    const val nameActivityDivName = "nameActivityDivName"
    const val namePdfMainTitle = "namePdfMainTitle"
    const val namePdfSubTitle = "namePdfSubTitle"
    const val nameIsFirstScreen = "nameIsFirstScreen"
    const val nameTitle = "nameTitle"
}
object CurrentDensity {
    var density : Float? = null
}

const val STATUS_NOT_DEFINED           = -1
const val STATUS_SUCCESS               = 200
const val STATUS_LOGIN_INVALID_ID_PW   = 403
const val STATUS_LOGIN_LIMITED_SERVICE = 406