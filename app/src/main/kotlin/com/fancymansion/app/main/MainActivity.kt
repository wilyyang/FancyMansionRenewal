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
            CurrentDensity.density = getDeviceScreenSize(typePane = typePane).let { (deviceWidth, deviceHeight) ->
                val (baseWidth, baseHeight, baseDensity) =
                    if (typePane == TypePane.TABLET) Triple(TABLET_BASE_SCREEN_WIDTH_PX, TABLET_BASE_SCREEN_HEIGHT_PX, TABLET_BASE_SCREEN_DENSITY)
                    else Triple(MOBILE_BASE_SCREEN_WIDTH_PX, MOBILE_BASE_SCREEN_HEIGHT_PX, MOBILE_BASE_SCREEN_DENSITY)

                val widthRatio = deviceWidth / baseWidth.toFloat()
                val heightRatio = deviceHeight / baseHeight.toFloat()
                baseDensity * (Math.min(heightRatio, widthRatio))
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

    /**
     * 기기의 화면 크기를 반환
     * 태블릿 세로모드는 긴 쪽을 너비로 계산하고 모바일 가로모드는 긴 쪽을 높이로 계산하기 위함
     * @param typePane 기기 유형 (TypePane)
     * @return 화면 크기 (ScreenSize)
     */
    private fun getDeviceScreenSize(typePane : TypePane): ScreenSize {
        val (height, width) = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).let { windowManager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                windowManager.currentWindowMetrics.bounds.let {
                    it.height() to it.width()
                }
            }else{
                val metrics = DisplayMetrics()
                windowManager.defaultDisplay.getRealMetrics(metrics)
                metrics.heightPixels to metrics.widthPixels
            }
        }
        val configOrientation = resources.configuration.orientation
        return if((typePane == TypePane.TABLET && configOrientation == Configuration.ORIENTATION_PORTRAIT)
            || (typePane == TypePane.MOBILE && configOrientation == Configuration.ORIENTATION_LANDSCAPE)) {
            ScreenSize(width = height, height = width)
        } else {
            ScreenSize(width = width, height = height)
        }
    }
}

data class ScreenSize(val width : Int, val height : Int)

