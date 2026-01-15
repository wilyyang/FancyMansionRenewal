package com.fancymansion.core.presentation.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fancymansion.core.common.R
import com.fancymansion.core.common.const.NetworkState
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.throwable.exception.InternetDisconnectException
import com.fancymansion.core.common.throwable.exception.RequestInternetCheckException
import com.fancymansion.core.common.throwable.ThrowableManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import java.io.File
import kotlin.coroutines.CoroutineContext

const val COMMON_EFFECTS_KEY = "COMMON_EFFECTS_KEY"
const val SIDE_EFFECTS_KEY = "SIDE_EFFECTS_KEY"

interface ViewState
interface ViewEvent
interface ViewSideEffect

sealed class LoadState {
    data object Init : LoadState()
    data object Idle : LoadState()
    data object Close : LoadState()
    data class Loading(val message : String? = null) : LoadState()
    data class ErrorDialog(
        val title : StringValue? = null,
        val message : StringValue? = null,
        val errorMessage : StringValue? = null,
        val backgroundColorCode : Int? = null,
        val confirmText : StringValue? = StringValue.Empty,
        val dismissText : StringValue? = StringValue.Empty,
        val onConfirm : () -> Unit = {},
        val onDismiss : () -> Unit = {}
    ) : LoadState(){
        constructor(
            title: String? = null,
            message: String? = null,
            errorMessage: String? = null,
            backgroundColorCode : Int? = null,
            confirmText: String? = "",
            dismissText: String? = "",
            onConfirm: () -> Unit = {},
            onDismiss: () -> Unit = {}
        ) : this(
            title = title?.let { StringValue.StringWrapper(title) },
            message = message?.let { StringValue.StringWrapper(message) },
            errorMessage = errorMessage?.let { StringValue.StringWrapper(errorMessage) },
            backgroundColorCode = backgroundColorCode,
            confirmText = confirmText?.let{ StringValue.StringWrapper(confirmText)},
            dismissText = dismissText?.let { StringValue.StringWrapper(dismissText) },
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
    data class AlarmDialog(
        val title : StringValue? = null,
        val message : StringValue? = null,
        val backgroundColorCode : Int? = null,
        val confirmText : StringValue? = StringValue.Empty,
        val dismissText : StringValue? = StringValue.Empty,
        val onConfirm : () -> Unit = {},
        val onDismiss : () -> Unit = {}
    ) : LoadState(){
        constructor(
            title: String? = null,
            message: String? = null,
            backgroundColorCode : Int? = null,
            confirmText: String? = "",
            dismissText: String? = "",
            onConfirm: () -> Unit = {},
            onDismiss: () -> Unit = {}
        ) : this(
            title = title?.let { StringValue.StringWrapper(title) },
            message = message?.let { StringValue.StringWrapper(message) },
            backgroundColorCode = backgroundColorCode,
            confirmText = confirmText?.let{ StringValue.StringWrapper(confirmText)},
            dismissText = dismissText?.let { StringValue.StringWrapper(dismissText) },
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

sealed class CommonEvent : ViewEvent {
    /**
     * Orientation
     */
    data object OrientationLandscapeEvent : CommonEvent()
    data object OrientationPortraitEvent : CommonEvent()
    /**
     * Compose Life Cycle
     */
    data object OnCreate : CommonEvent()
    data object OnResume : CommonEvent()
    data object OnPause : CommonEvent()
    data object OnDestroy : CommonEvent()
    /**
     * Permission
     */
    class PermissionCheckAndRequest(val permissions : Array<String>) : CommonEvent()
    class PermissionRequestAllGranted(val permissions : Array<String>) : CommonEvent()
    class PermissionRequestDenied(val deniedPermissions : Array<String>) : CommonEvent()
    data object PermissionSettingResultDenied : CommonEvent()
    /**
     * etc
     */
    data object CloseEvent : CommonEvent()
    data object ApplicationExitEvent : CommonEvent()
    data class OpenWebBrowser(val url :String) : CommonEvent()
    data class SendNetworkState(val state: NetworkState) : CommonEvent()
    class RequestInternetExceptionResultEvent(val throwable: Throwable) : CommonEvent()
}

sealed class CommonEffect : ViewSideEffect {
    /**
     * Permission
     */
    class PermissionCheckAndRequestEffect(val permissions : Array<String>) : CommonEffect()
    class PermissionMoveSettingAppEffect(val permissions : Array<String>) : CommonEffect()

    /**
     * etc
     */
    data class OpenWebBrowser(val url :String) : CommonEffect()
    data object RequestNetworkState : CommonEffect()
    class RequestInternetCheckExceptionEffect(val throwable: RequestInternetCheckException) : CommonEffect()
    class SendLogToEmailEffect(val logFile : File) : CommonEffect()

    sealed class Navigation : CommonEffect() {
        data object NavigateBack : Navigation()
        data object NavigateApplicationExit : Navigation()
        data object NavigateMain : Navigation()
        data object NavigateLogin : Navigation()
    }
}

abstract class BaseViewModel<UiState : ViewState, Event : ViewEvent, Effect : ViewSideEffect> : ViewModel() {

    protected val scope = viewModelScope

    /**
     * BaseViewModel을 상속하는 ViewModel은
     * UiState의 초기값을 할당하는 setInitialState 와
     * 전달받은 Event를 처리할 handleEvents 를 구현해야 한다.
     */
    abstract fun setInitialState() : UiState

    abstract fun handleEvents(event : Event)

    private val initialState : UiState by lazy { setInitialState() }

    /**
     * _uiState : Screen에 전달할 데이터
     * _loadState : Screen에 dialog로 호출되는 공통 요소의 상태
     * _event : Screen 으로부터 전달 받을 이벤트
     * _effect : Screen 에 전달할 이펙트
     */
    private val _uiState : MutableState<UiState> = mutableStateOf(initialState)
    private val _loadState : MutableState<LoadState> = mutableStateOf(LoadState.Init)
    private val _event : MutableSharedFlow<Event> = MutableSharedFlow(extraBufferCapacity = 1)
    private val _effect : Channel<Effect> = Channel(Channel.BUFFERED)

    private val _commonEvent : MutableSharedFlow<CommonEvent> = MutableSharedFlow(extraBufferCapacity = 1)
    private val _commonEffect : Channel<CommonEffect> = Channel(Channel.BUFFERED)

    val uiState : State<UiState> = _uiState
    val loadState : State<LoadState> = _loadState
    val effect = _effect.receiveAsFlow().shareIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        replay = 0
    )

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
                Logger.print(message = "<CommonEvent> ", value = it, tag = Logger.BASIC_TAG_NAME)
                handleCommonEvents(it)
            }
        }

        scope.launch {
            _event.collect {
                Logger.print(message = "<Event> ", value = it, tag = Logger.BASIC_TAG_NAME)
                handleEvents(it)
            }
        }
    }

    /**
     * State, Event, Effect의 Setter
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
        Logger.print(message = "<Effect> ", value = effectValue, tag = Logger.BASIC_TAG_NAME)
        scope.launch { _effect.send(effectValue) }
    }

    protected fun setCommonEffect(builder : () -> CommonEffect) {
        val effectValue = builder()
        Logger.print(message = "<CommonEffect> ", value = effectValue, tag = Logger.BASIC_TAG_NAME)
        scope.launch { _commonEffect.send(effectValue) }
    }

    /**
     * handleCommonEvents : ViewModel 공통 이벤트를 처리하는 메소드
     */
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
                val denied = listOf(event.deniedPermissions.reduce { acc, s -> "-$acc\n-$s" })
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

            /**
             * etc
             */
            is CommonEvent.CloseEvent -> {
                setLoadState(LoadState.Close)
                setCommonEffect{
                    CommonEffect.Navigation.NavigateBack
                }
            }
            is CommonEvent.ApplicationExitEvent -> {
                setCommonEffect{
                    CommonEffect.Navigation.NavigateApplicationExit
                }
            }
            is CommonEvent.OpenWebBrowser -> {
                setCommonEffect {
                    CommonEffect.OpenWebBrowser(event.url)
                }
            }
            is CommonEvent.RequestInternetExceptionResultEvent -> {
                handleException(event.throwable)
            }

            else -> {}
        }
    }

    /**
     * 작업 처리를 위한 블록
     * - launchWithInit : 시작할 때 Init, 끝나면 Idle 상태
     * - launchWithLoading : 시작할 때 Loading, 끝나면 Idle 상태
     */
    fun launchWithInit(
        context : CoroutineContext = Dispatchers.IO,
        start : CoroutineStart = CoroutineStart.DEFAULT,
        timeoutMs : Long? = null,
        endLoadState: LoadState? = LoadState.Idle,
        block : suspend CoroutineScope.() -> Unit
    ) : Job {
        return launchWithException(
            context = context,
            start = start,
            timeoutMs = timeoutMs,
            startLoadState = LoadState.Init,
            endLoadState = endLoadState,
            block = block
        )
    }

    fun launchWithLoading(
        context : CoroutineContext = Dispatchers.IO,
        start : CoroutineStart = CoroutineStart.DEFAULT,
        timeoutMs : Long? = null,
        endLoadState: LoadState? = LoadState.Idle,
        block : suspend CoroutineScope.() -> Unit
    ) : Job {
        return launchWithException(
            context = context,
            start = start,
            timeoutMs = timeoutMs,
            startLoadState = LoadState.Loading(),
            endLoadState = endLoadState,
            block = block
        )
    }

    fun launchWithException(
        context : CoroutineContext = Dispatchers.IO,
        start : CoroutineStart = CoroutineStart.DEFAULT,
        timeoutMs : Long? = null,
        startLoadState: LoadState? = null,
        endLoadState: LoadState? = null,
        block : suspend CoroutineScope.() -> Unit
    ) : Job {
        return scope.launch(
            context + CoroutineExceptionHandler { _, t ->
                if (t is CancellationException) return@CoroutineExceptionHandler
                handleException(t)
            },
            start
        ) {
            startLoadState?.let {
                withContext(Dispatchers.Main) {
                    _loadState.value = startLoadState
                }
            }

            var success = false
            try {
                if (timeoutMs != null) withTimeout(timeoutMs) { block() } else block()
                success = true
            } finally {
                if (success) {
                    endLoadState?.let {
                        withContext(Dispatchers.Main) {
                            _loadState.value = endLoadState
                        }
                    }
                }
            }
        }.apply {
            invokeOnCompletion { cause ->
                cause?.let {
                    when (cause) {
                        is TimeoutCancellationException -> handleException(cause)
                        is CancellationException -> showExceptionResult(cause)
                    }
                }
            }
        }
    }

    /**
     * 에러 처리
     */
    protected fun handleException(throwable: Throwable) {
        if(throwable is RequestInternetCheckException){
            setCommonEffect {
                CommonEffect.RequestInternetCheckExceptionEffect(throwable)
            }
        }else {
            ThrowableManager.handleError(throwable)
            showExceptionResult(throwable)
        }
    }

    protected open fun showExceptionResult(
        throwable: Throwable,
        defaultConfirm : ()-> Unit = { setCommonEvent (CommonEvent.CloseEvent)  },
        defaultDismiss : ()-> Unit = ::setLoadStateIdle
    ){
        scope.launch(Dispatchers.Main.immediate) {
            _loadState.value = when(throwable){
                is InternetDisconnectException -> LoadState.ErrorDialog(
                    message = StringValue.StringResource(R.string.dialog_network_error),
                    dismissText = null,
                    onConfirm = defaultConfirm
                )

                is TimeoutCancellationException -> LoadState.ErrorDialog(
                    message = StringValue.StringResource(R.string.dialog_error_back),
                    errorMessage = StringValue.StringWrapper(
                        throwable.message ?: "TimeoutCancellationException"
                    ),
                    dismissText = null,
                    onConfirm = defaultConfirm
                )

                is CancellationException ->  {
                    if (_loadState.value is LoadState.Loading || _loadState.value is LoadState.Init) {
                        LoadState.Idle
                    } else {
                        _loadState.value
                    }
                }

                else -> LoadState.ErrorDialog(
                    message = StringValue.StringResource(R.string.dialog_error_back),
                    errorMessage = StringValue.StringWrapper(
                        throwable.message ?: throwable::class.java.simpleName
                    ),
                    dismissText = null,
                    onConfirm = defaultConfirm
                )
            }
        }
    }
}