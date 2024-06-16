package com.fancymansion.core.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fancymansion.core.common.R
import com.fancymansion.core.common.const.NetworkState
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.throwable.ApiNetworkException
import com.fancymansion.core.common.throwable.ApiResultStatusException
import com.fancymansion.core.common.throwable.ApiUnknownException
import com.fancymansion.core.common.throwable.InternetDisconnectException
import com.fancymansion.core.common.throwable.RequestInternetCheckException
import com.fancymansion.core.common.throwable.ScreenDataInitFailException
import com.fancymansion.core.common.throwable.ThrowableManager
import com.fancymansion.core.common.throwable.WebViewException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import java.io.File
import java.io.IOException
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

interface ViewState
interface ViewEvent
interface ViewSideEffect

sealed class LoadState {
    object Idle : LoadState()
    data class Loading(val message : String? = null) : LoadState()
    data class ErrorDialog(
        val title : StringValue? = null,
        val message : StringValue? = null,
        val errorMessage : StringValue? = null,
        val confirmText: StringValue = StringValue.Empty,
        val dismissText: StringValue? = StringValue.Empty,
        val onConfirm : () -> Unit = {},
        val onDismiss : () -> Unit = {}
    ) : LoadState(){
        constructor(
            title: String? = null,
            message: String? = null,
            errorMessage: String? = null,
            confirmText: String = "",
            dismissText: String? = "",
            onConfirm: () -> Unit = {},
            onDismiss: () -> Unit = {}
        ) : this(
            title = title?.let { StringValue.DynamicString(title) },
            message = message?.let { StringValue.DynamicString(message) },
            errorMessage = errorMessage?.let { StringValue.DynamicString(errorMessage) },
            confirmText = StringValue.DynamicString(confirmText),
            dismissText = dismissText?.let { StringValue.DynamicString(dismissText) },
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
    data class AlarmDialog(
        val title : StringValue? = null,
        val message : StringValue? = null,
        val confirmText : StringValue? = StringValue.Empty,
        val dismissText : StringValue? = StringValue.Empty,
        val onConfirm : () -> Unit = {},
        val onDismiss : () -> Unit = {}
    ) : LoadState(){
        constructor(
            title: String? = null,
            message: String? = null,
            confirmText: String? = "",
            dismissText: String? = "",
            onConfirm: () -> Unit = {},
            onDismiss: () -> Unit = {}
        ) : this(
            title = title?.let { StringValue.DynamicString(title) },
            message = message?.let { StringValue.DynamicString(message) },
            confirmText = confirmText?.let{ StringValue.DynamicString(confirmText)},
            dismissText = dismissText?.let { StringValue.DynamicString(dismissText) },
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }

    class CustomDialog(
        val customDialog : @Composable () -> Unit
    ) : LoadState()
}

sealed class CommonEvent : ViewEvent {
    /**
     * Orientation
     */
    object OrientationLandscapeEvent : CommonEvent()
    object OrientationPortraitEvent : CommonEvent()

    /**
     * Permission
     */
    class PermissionCheckAndRequest(val permissions : Array<String>) : CommonEvent()
    class PermissionRequestAllGranted(val permissions : Array<String>) : CommonEvent()
    class PermissionRequestDenied(val deniedPermissions : Array<String>) : CommonEvent()
    object PermissionSettingResultDenied : CommonEvent()

    /**
     * Life Cycle
     */
    object OnCreate : CommonEvent()
    object OnResume : CommonEvent()
    object OnPause : CommonEvent()
    object OnDestroy : CommonEvent()

    /**
     * etc
     */
    object CloseEvent : CommonEvent()
    object ApplicationExitEvent : CommonEvent()
    class ReceiveFirebaseToken(val token: String?) : CommonEvent()

    data class SendToHelpEmail(val loginId: String?) : CommonEvent()
    data class SendNetworkState(val state: NetworkState) : CommonEvent()
    class RequestInternetExceptionResultEvent(val throwable: Throwable) : CommonEvent()
    data class OpenWebBrowser(val url :String) : CommonEvent()
}

sealed class CommonEffect : ViewSideEffect {
    class PermissionCheckAndRequestEffect(val permissions : Array<String>) : CommonEffect()
    class PermissionMoveSettingAppEffect(val permissions : Array<String>) : CommonEffect()
    data class SendToHelpEmail(val loginId : String?) : CommonEffect()
    class SendLogToEmailEffect(val logFile : File) : CommonEffect()
    object RequestFirebaseToken : CommonEffect()
    object RequestNetworkState : CommonEffect()
    class HandleRequestInternetExceptionEffect(val throwable: RequestInternetCheckException) : CommonEffect()
    data class OpenWebBrowser(val url :String) : CommonEffect()

    sealed class Navigation : CommonEffect() {
        object NavigateLogin : Navigation()
        object NavigateMain : Navigation()
        object NavigateBack : Navigation()
        object NavigateApplicationExit : Navigation()
        data class NavigateWebBrowser(val url : String) : Navigation()
        data class NavigatePlayStore(val uri : String) : Navigation()
    }
}

const val COMMON_EFFECTS_KEY = "common_effects_key"
const val SIDE_EFFECTS_KEY = "side_effects_key"
const val DEFAULT_DELAY_TIME = 15000L

abstract class BaseViewModel<UiState : ViewState, Event : ViewEvent, Effect : ViewSideEffect> : ViewModel() {

    private val rootContext: CoroutineContext
        get() = viewModelScope.coroutineContext + CoroutineExceptionHandler { _, throwable ->
            sendError(throwable)
        }

    protected val scope = CoroutineScope(rootContext)

    /**
     * BaseViewModel 을 상속하는 ViewModel은
     * UiState의 초기값을 할당하는 setInitialState 와
     * 전달받은 Event를 처리할 handleEvents 를 구현해야 한다.
     */
    abstract fun setInitialState() : UiState
    abstract fun handleEvents(event : Event)
    open fun handleCommonEvents(event : CommonEvent){
        when(event){

            /**
             * Permission
             */
            is CommonEvent.PermissionCheckAndRequest -> {
                setCommonEffect{
                    CommonEffect.PermissionCheckAndRequestEffect(event.permissions)
                }
            }

            is CommonEvent.PermissionRequestDenied -> {
                val denied = event.deniedPermissions.reduce { acc, s -> "-$acc\n-$s" }
                setLoadState(LoadState.AlarmDialog(
                    title = StringValue.StringResource(R.string.permission_required_title),
                    message = StringValue.StringResource(
                        R.string.permission_required_message,
                        denied
                    ),
                    confirmText = StringValue.StringResource(R.string.move_screen),
                    dismissText = StringValue.StringResource(R.string.cancel),
                    onConfirm = {
                        setCommonEffect {
                            CommonEffect.PermissionMoveSettingAppEffect(
                                event.deniedPermissions
                            )
                        }
                    },
                    onDismiss = { setCommonEvent(CommonEvent.CloseEvent) }
                ))
            }

            is CommonEvent.PermissionSettingResultDenied -> {
                scope.launch {
                    delay(500)
                    setLoadState(LoadState.ErrorDialog(
                        title = StringValue.StringResource(R.string.permission_denied_title),
                        message = StringValue.StringResource(R.string.permission_denied_message),
                        confirmText = StringValue.StringResource(R.string.move_screen),
                        dismissText = null,
                        onConfirm = { setCommonEvent(CommonEvent.CloseEvent) }
                    ))
                }
            }

            is CommonEvent.ApplicationExitEvent -> {
                setCommonEffect{
                    CommonEffect.Navigation.NavigateApplicationExit
                }
            }

            /**
             * etc
             */
            is CommonEvent.CloseEvent -> {
                setCommonEffect{
                    CommonEffect.Navigation.NavigateBack
                }
            }

            is CommonEvent.RequestInternetExceptionResultEvent -> {
                sendError(event.throwable)
            }

            is CommonEvent.OpenWebBrowser -> {
                setCommonEffect {
                    CommonEffect.OpenWebBrowser(event.url)
                }
            }

            else -> {}
        }
    }

    private val initialState : UiState by lazy { setInitialState() }

    /**
     * _uiState : Screen에 전달할 데이터
     * _loadState : Screen에 dialog로 호출되는 공통 요소의 상태
     * _event : Screen 으로부터 전달 받을 이벤트
     * _effect : Screen 에 전달할 이펙트
     */
    private val _uiState : MutableState<UiState> = mutableStateOf(initialState)
    private val _loadState : MutableState<LoadState> = mutableStateOf(LoadState.Idle)
    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    private val _effect : Channel<Effect> = Channel()

    private val _commonEvent : MutableSharedFlow<CommonEvent> = MutableSharedFlow()
    private val _commonEffect : Channel<CommonEffect> = Channel()

    val uiState : State<UiState> = _uiState
    val loadState : State<LoadState> = _loadState
    val effect = _effect.receiveAsFlow().shareIn(scope = scope, started = WhileSubscribed())

    val commonEffect = _commonEffect.receiveAsFlow()

    /**
     * Event는 ViewModel 에서 수집하여 처리한다
     */
    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        scope.launch {
            _commonEvent.collect {
                Logger.print(message = "<Event> ${it::class.simpleName}", tag = Logger.BASIC_TAG_NAME)
                handleCommonEvents(it)
            }
        }

        scope.launch {
            _event.collect {
                Logger.print(message = "<Event> ${it::class.simpleName}", tag = Logger.BASIC_TAG_NAME)
                handleEvents(it)
            }
        }
    }

    /**
     * UiState, Event, Effect의 Setter
     */
    protected fun setState(reducer : UiState.() -> UiState) {
        val newState = uiState.value.reducer()
        _uiState.value = newState
    }

    protected fun setLoadState(loadState : LoadState) {
        _loadState.value = loadState
    }
    protected fun setLoadStateIdle() {
        _loadState.value = LoadState.Idle
    }

    fun setEvent(event : Event) {
        scope.launch { _event.emit(event) }
    }

    fun setCommonEvent(event : CommonEvent) {
        scope.launch { _commonEvent.emit(event) }
    }

    protected fun setEffect(builder : () -> Effect) {
        val effectValue = builder()
        Logger.print(message = "<Effect> ${effectValue::class.simpleName}", tag = Logger.BASIC_TAG_NAME)
        scope.launch { _effect.send(effectValue) }
    }

    protected fun setCommonEffect(builder : () -> CommonEffect) {
        val effectValue = builder()
        Logger.print(message = "<Effect> ${effectValue::class.simpleName}", tag = Logger.BASIC_TAG_NAME)
        scope.launch { _commonEffect.send(effectValue) }
    }

    /**
     * 로딩
     */
    fun launchWithLoading(
        context : CoroutineContext = Dispatchers.IO,
        start : CoroutineStart = CoroutineStart.DEFAULT,
        delayTime : Long = DEFAULT_DELAY_TIME,
        endLoadState: LoadState? = LoadState.Idle,
        block : suspend CoroutineScope.() -> Unit
    ) : Job {
        return scope.launch(context, start) {
            withContext(Dispatchers.Main) {
                _loadState.value = LoadState.Loading()
                withContext(context = context) {
                    withTimeout(delayTime) {
                        block.invoke(this)
                    }
                }
                endLoadState?.let {
                    _loadState.value = endLoadState
                }
            }
        }.apply {
            invokeOnCompletion { cause : Throwable? ->
                cause?.also { cancelException ->
                    if(cancelException.cause != null){
                        sendError(cancelException.cause!!)
                    }else{
                        sendError(cancelException)
                    }
                }
            }
        }
    }

    /**
     * Exception 추가
     */
    fun launchWithException(
        context : CoroutineContext = Dispatchers.IO,
        start : CoroutineStart = CoroutineStart.DEFAULT,
        delayTime : Long = DEFAULT_DELAY_TIME,
        block : suspend CoroutineScope.() -> Unit
    ) : Job {
        return scope.launch(context, start) {
            withContext(Dispatchers.Main) {
                withContext(context = context) {
                    withTimeout(delayTime) {
                        block.invoke(this)
                    }
                }
            }
        }.apply {
            invokeOnCompletion { cause : Throwable? ->
                cause?.also { cancelException ->
                    if(cancelException.cause != null){
                        sendError(cancelException.cause!!)
                    }else{
                        sendError(cancelException)
                    }
                }
            }
        }
    }

    /**
     * 에러 처리
     */
    protected fun sendError(throwable: Throwable) {
        if(throwable is RequestInternetCheckException){
            setCommonEffect {
                CommonEffect.HandleRequestInternetExceptionEffect(throwable)
            }
        }else {
            ThrowableManager.handleError(throwable)
            showErrorResult(throwable)
        }
    }

    protected open fun showErrorResult(
        throwable: Throwable,
        defaultConfirm : ()-> Unit = { setCommonEvent (CommonEvent.CloseEvent)  },
        defaultDismiss : ()-> Unit = ::setLoadStateIdle
    ){
        _loadState.value = when(throwable){
            is WebViewException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_error_back),
                errorMessage = StringValue.DynamicString(
                    throwable.result.message ?: "WebViewException"
                ),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            is ScreenDataInitFailException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_error_back),
                errorMessage = StringValue.DynamicString(throwable.message),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            is ApiNetworkException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_error_back),
                errorMessage = StringValue.DynamicString(throwable.message),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            is ApiUnknownException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_error_back),
                errorMessage = StringValue.DynamicString(throwable.message),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            is ApiResultStatusException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_error_back),
                errorMessage = StringValue.DynamicString(throwable.message),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            is InternetDisconnectException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_network_error),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            is CancellationException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_error_back),
                errorMessage = StringValue.DynamicString(
                    throwable.message ?: "Unknown CancellationException"
                ),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            // 인터넷 연결 오류
            is IOException -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_network_error),
                dismissText = null,
                onConfirm = defaultConfirm
            )

            else -> LoadState.ErrorDialog(
                message = StringValue.StringResource(R.string.dialog_error_back),
                errorMessage = StringValue.DynamicString(
                    throwable.message ?: throwable::class.java.simpleName
                ),
                dismissText = null,
                onConfirm = defaultConfirm
            )
        }
    }
}