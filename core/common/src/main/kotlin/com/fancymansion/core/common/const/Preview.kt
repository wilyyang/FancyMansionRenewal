package com.fancymansion.core.common.const

const val DESIGN_STATUS_BAR_PX = 72
const val DESIGN_BOTTOM_PX = 28

const val MOBILE_BASE_SCREEN_HEIGHT_NO_BAR_PX = MOBILE_BASE_SCREEN_HEIGHT_PX - DESIGN_STATUS_BAR_PX - DESIGN_BOTTOM_PX

const val MOBILE_BASE_SCREEN_HEIGHT_DP = (MOBILE_BASE_SCREEN_HEIGHT_PX / MOBILE_BASE_SCREEN_DENSITY).toInt()
const val MOBILE_BASE_SCREEN_HEIGHT_NO_BAR_DP = (MOBILE_BASE_SCREEN_HEIGHT_NO_BAR_PX / MOBILE_BASE_SCREEN_DENSITY).toInt()
const val MOBILE_BASE_SCREEN_WIDTH_DP = (MOBILE_BASE_SCREEN_WIDTH_PX / MOBILE_BASE_SCREEN_DENSITY).toInt()


const val MOBILE_PREVIEW_SPEC = "spec:id=reference_phone,shape=Normal," +
        "width=$MOBILE_BASE_SCREEN_WIDTH_DP," +
        "height=$MOBILE_BASE_SCREEN_HEIGHT_NO_BAR_DP,unit=dp," +
        "dpi=$MOBILE_BASE_SCREEN_DPI"

const val MOBILE_LANDSCAPE_PREVIEW_SPEC = "spec:id=reference_phone,shape=Normal," +
        "width=$MOBILE_BASE_SCREEN_HEIGHT_NO_BAR_DP," +
        "height=$MOBILE_BASE_SCREEN_WIDTH_DP,unit=dp," +
        "dpi=$MOBILE_BASE_SCREEN_DPI"

const val TABLET_BASE_SCREEN_HEIGHT_NO_BAR_PX = TABLET_BASE_SCREEN_HEIGHT_PX - DESIGN_STATUS_BAR_PX - DESIGN_BOTTOM_PX

const val TABLET_BASE_SCREEN_HEIGHT_DP = (TABLET_BASE_SCREEN_HEIGHT_PX / TABLET_BASE_SCREEN_DENSITY).toInt()
const val TABLET_BASE_SCREEN_HEIGHT_NO_BAR_DP = (TABLET_BASE_SCREEN_HEIGHT_NO_BAR_PX / TABLET_BASE_SCREEN_DENSITY).toInt()
const val TABLET_BASE_SCREEN_WIDTH_DP = (TABLET_BASE_SCREEN_WIDTH_PX / TABLET_BASE_SCREEN_DENSITY).toInt()

const val TABLET_PREVIEW_SPEC = "spec:shape=Normal," +
        "width=$TABLET_BASE_SCREEN_WIDTH_DP," +
        "height=$TABLET_BASE_SCREEN_HEIGHT_DP,unit=dp," +
        "dpi=$TABLET_BASE_SCREEN_DPI"