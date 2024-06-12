package com.fancymansion.core.presentation.util

import android.webkit.WebView
import com.fancymansion.core.common.const.DURATION_SHORTER

fun WebView.postLoadUrl(url : String, duration : Long = DURATION_SHORTER) =
    this.postDelayed({loadUrl(url)}, duration)