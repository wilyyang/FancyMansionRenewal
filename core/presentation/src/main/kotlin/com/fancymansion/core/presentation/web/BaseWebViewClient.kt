package com.fancymansion.core.presentation.web

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.common.wrapper.ApiError

open class BaseWebViewClient : WebViewClient() {
    open lateinit var state: WebViewState

    private val timeoutHandler = TimeOutHandler()
    private class TimeOutHandler : Handler(Looper.getMainLooper())
    private var timeoutCheckStarted = false

    private val runnable = Runnable {
        state.webErrors.add(ApiError(0,"TIME OUT"))
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        if(!timeoutCheckStarted) {
            Logger.i("WebView timeout Start")
            timeoutHandler.postDelayed(runnable, 15000)
            timeoutCheckStarted = true
        }
        super.onLoadResource(view, url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Logger.i("WebView page Start")
        state.loadingState = WebLoadingState.Loading
        state.webErrors.clear()
        state.lastLoadedUrl = url
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Logger.i("WebView page finished")
        state.loadingState = WebLoadingState.Finished
        timeoutHandler.removeCallbacks(runnable)
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (error != null) {
            Logger.i("WebView onReceivedError ${error.description}")
            timeoutHandler.removeCallbacks(runnable)
            state.webErrors.add(ApiError(error.errorCode, error.description.toString()))
        }
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        if (errorResponse != null) {
            Logger.i("WebView onReceivedHttpError ${errorResponse.reasonPhrase}")
            timeoutHandler.removeCallbacks(runnable)
            state.webErrors.add(ApiError(errorResponse.statusCode, errorResponse.reasonPhrase))
        }
    }


}

@Stable
class WebViewState {
    var webView by mutableStateOf<WebView?>(null)
    var lastLoadedUrl: String? by mutableStateOf(null)
        internal set

    var loadingState: WebLoadingState by mutableStateOf(WebLoadingState.Initializing)
        internal set
    val isLoading: Boolean get() = loadingState !is WebLoadingState.Finished

    val webErrors: SnapshotStateList<ApiError> = mutableStateListOf()
}

sealed class WebLoadingState {
    object Initializing : WebLoadingState()
    object Loading : WebLoadingState()
    object Finished : WebLoadingState()
}
