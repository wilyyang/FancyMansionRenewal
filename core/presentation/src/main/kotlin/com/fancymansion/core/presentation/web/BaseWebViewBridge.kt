package com.fancymansion.core.presentation.web

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.fancymansion.core.common.const.DURATION_SHORTER
import com.fancymansion.core.common.log.Logger

open class BaseWebViewBridge(
    open val webView : WebView,
    val onShowLoading : () -> Unit = { },
    val onHideLoading : () -> Unit = { },
    val onNavigateBack : () -> Unit = {},
    val onShowPopup : (String, String?, String?) -> Unit = { message, confirmText, dismissText -> },
    val onOpenWebBrowser : (String?) -> Unit = { url -> }
) {
    companion object {
        val requestPopupOkClicked : String
            get() {
                Logger.i("Load javascript:window.confirmPopup(\"Y\")")
                return "javascript:window.confirmPopup(\"Y\")"
            }

        val requestPopupCancelClicked : String
            get() {
                Logger.i("Load javascript:window.confirmPopup(\"N\")")
                return "javascript:window.confirmPopup(\"N\")"
            }
    }

    @JavascriptInterface
    fun appInterfaceShowSpinner() {
        Logger.i("Call appInterfaceShowSpinner")
        webView.postDelayed({ onShowLoading() }, DURATION_SHORTER)

    }

    @JavascriptInterface
    fun appInterfaceHideSpinner() {
        Logger.i("Call appInterfaceHideSpinner")
        webView.postDelayed({ onHideLoading() }, DURATION_SHORTER)
    }

    @JavascriptInterface
    fun appInterfaceNavigateBack() {
        Logger.i("Call appInterfaceNavigateBack")
        webView.postDelayed({ onNavigateBack() }, DURATION_SHORTER)
    }

    @JavascriptInterface
    fun appInterfaceShowPopup(contents : Array<String>) {
        Logger.i("Call appInterfaceShowPopup List size: ${contents.size}")

        if(contents.isEmpty()) return
        contents.let {
            webView.postDelayed({
                onHideLoading()
                onShowPopup(it[0],if(it.size>=2) it[1] else null ,if(it.size>=3) it[2] else null)
            }, DURATION_SHORTER)
        }
    }

    @JavascriptInterface
    fun appInterfaceOpenUrl(url : String) {
        Logger.i("Call appInterfaceOpenUrl $url")
        webView.postDelayed({ onOpenWebBrowser(url) }, DURATION_SHORTER)
    }
}