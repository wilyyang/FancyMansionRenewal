package com.fancymansion.core.presentation.window

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsCompat
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.common.util.isTablet

object Feature {
    fun getTypeWindow(context : Context) : TypeWindow {
       return if (isTablet(context)) {
           Logger.i("isTablet : true")
           TypeWindow(TypePane.DUAL, TypeOrientation.LANDSCAPE)
       } else {
           Logger.i("isTablet : false")
           TypeWindow(TypePane.SINGLE, TypeOrientation.PORTRAIT)
       }
    }

    fun setOrientation(activity: Activity, typeOrientation: TypeOrientation) {
        activity.requestedOrientation = when (typeOrientation) {
            TypeOrientation.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            else -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    fun hideSystemUI(window: Window) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

        if (Build.VERSION.SDK_INT >= 30) {
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            window.insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
            window.insetsController?.hide(WindowInsetsCompat.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    fun showSystemUI(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    @Composable
    fun findWindow(): Window? =
        (LocalView.current.parent as? DialogWindowProvider)?.window
            ?: LocalView.current.context.findWindow()

    private tailrec fun Context.findWindow(): Window? =
        when (this) {
            is Activity -> window
            is ContextWrapper -> baseContext.findWindow()
            else -> null
        }
}