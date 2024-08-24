package com.fancymansion.app.main

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fancymansion.app.navigation.AppScreenConfiguration
import com.fancymansion.core.common.const.CurrentDensity
import com.fancymansion.core.common.const.MOBILE_BASE_SCREEN_DENSITY
import com.fancymansion.core.common.const.MOBILE_BASE_SCREEN_HEIGHT_PX
import com.fancymansion.core.common.const.MOBILE_BASE_SCREEN_WIDTH_PX
import com.fancymansion.core.common.const.TABLET_BASE_SCREEN_DENSITY
import com.fancymansion.core.common.const.TABLET_BASE_SCREEN_HEIGHT_PX
import com.fancymansion.core.common.const.TABLET_BASE_SCREEN_WIDTH_PX
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.presentation.compose.theme.FancyMansionTheme
import com.fancymansion.core.presentation.compose.theme.typography.typographyMobile
import com.fancymansion.core.presentation.compose.theme.typography.typographyTablet
import com.fancymansion.core.presentation.base.window.Feature
import com.fancymansion.core.presentation.base.window.TypeOrientation
import com.fancymansion.core.presentation.base.window.TypePane
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        val (typePane, typeOrientation) = Feature.getTypeWindow(context = this)
        val typography = if(typePane == TypePane.MOBILE){
            typographyMobile
        }else{
            typographyTablet
        }

        if (CurrentDensity.density == null) {
            CurrentDensity.density = getScreenSize(typePane == TypePane.TABLET).let { (deviceWidth, deviceHeight) ->
                val (baseWidth, baseHeight, baseDensity) =
                    if (typePane == TypePane.TABLET) Triple(TABLET_BASE_SCREEN_WIDTH_PX, TABLET_BASE_SCREEN_HEIGHT_PX, TABLET_BASE_SCREEN_DENSITY)
                    else Triple(MOBILE_BASE_SCREEN_WIDTH_PX, MOBILE_BASE_SCREEN_HEIGHT_PX, MOBILE_BASE_SCREEN_DENSITY)

                val widthRatio = deviceWidth / baseWidth.toFloat()
                val heightRatio = deviceHeight / baseHeight.toFloat()
                baseDensity * heightRatio.coerceAtMost(widthRatio)
            }
        }

        mapOf(
            "app_version" to this.packageManager.getPackageInfo(this.packageName, 0).versionName,
            "device_name" to Build.MODEL,
            "os_name" to "android",
            "os_version" to Build.VERSION.RELEASE,
            "device_type" to if(typePane == TypePane.MOBILE) "phone" else "tablet",
            "device_density" to this.resources.displayMetrics.density,
            "current_density" to CurrentDensity.density
        ).forEach { (item, value) ->
            Logger.print(message = "[$item] $value", tag = Logger.BASIC_TAG_NAME)
        }

        setContent {
            FancyMansionTheme(
                typography = typography
            ) {
                AppScreenConfiguration(typePane, CurrentDensity.density!!)
            }
        }

        Feature.setOrientation(activity = this, typeOrientation = typeOrientation)
        if(typeOrientation != TypeOrientation.PORTRAIT){
            Feature.hideSystemUi(window)

            /**
             * SDK < 30 에서
             * TextField, DropDown 이 Focus 가지면 하단 네비게이션 바가 다시 보이고
             * 이후에도 지속되는 문제 해결을 위한 코드
             */
            if (Build.VERSION.SDK_INT < 30) {
                val decorView = window.decorView
                decorView.setOnSystemUiVisibilityChangeListener {
                    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                }
            }
        }
    }
    private fun getScreenSize(isTablet:Boolean): ScreenSize {
        return (getSystemService(Context.WINDOW_SERVICE) as WindowManager).let { windowManager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val bounds = windowManager.currentWindowMetrics.bounds
                if((isTablet && resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    || (!isTablet && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                    ScreenSize(width = bounds.height(), height = bounds.width())
                } else {
                    ScreenSize(width = bounds.width(), height = bounds.height())
                }
            } else {
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getRealMetrics(displayMetrics)
                if((isTablet && resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    || (!isTablet && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                    ScreenSize(width = displayMetrics.heightPixels, height = displayMetrics.widthPixels)
                } else {
                    ScreenSize(width = displayMetrics.widthPixels, height = displayMetrics.heightPixels)
                }
            }
        }
    }
}

data class ScreenSize(val width : Int, val height : Int)

