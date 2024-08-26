package com.fancymansion.app.navigation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.fancymansion.core.common.const.NetworkState
import com.fancymansion.core.common.throwable.exception.ApiUnknownException
import com.fancymansion.core.common.throwable.exception.InternetDisconnectException
import com.fancymansion.core.presentation.base.COMMON_EFFECTS_KEY
import com.fancymansion.core.presentation.base.CommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Composable
fun HandleCommonEffect(
    navController: NavController,
    commonEffectFlow: Flow<CommonEffect>,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    val context = LocalContext.current

    /**
     * LauncherForActivityResult
     *
     * @launcherPermissions : Permission Dialog Result
     * @launcherSetting : System Setting App Result
     */
    val launcherPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->

        val deniedPermissions = permissionsResult.filter {
            !it.value
        }.keys.toTypedArray()

        if (deniedPermissions.isEmpty()) {
            onCommonEventSent(CommonEvent.PermissionRequestAllGranted(permissionsResult.filter { it.value }.keys.toTypedArray()))
        } else {
            onCommonEventSent(CommonEvent.PermissionRequestDenied(deniedPermissions))
        }
    }

    val settingsPermission  = remember { mutableStateOf<Array<String>?>(null) }
    val launcherSettingApp = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val isAllGranted = settingsPermission.value?.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }?:false
        settingsPermission.value = null

        if (isAllGranted) {
            onCommonEventSent(CommonEvent.PermissionRequestAllGranted(settingsPermission.value?: emptyArray()))
        } else {
            onCommonEventSent(CommonEvent.PermissionSettingResultDenied)
        }
    }

    /**
     * Handle Effect
     */
    LaunchedEffect(COMMON_EFFECTS_KEY) {
        commonEffectFlow.onEach { effect ->
            when (effect) {
                /**
                 * Permission
                 */
                is CommonEffect.PermissionCheckAndRequestEffect -> {
                    val isAllGranted = effect.permissions.all {
                        ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
                    }

                    if (isAllGranted) {
                        onCommonEventSent(CommonEvent.PermissionRequestAllGranted(effect.permissions))
                    } else {
                        launcherPermissions.launch(effect.permissions)
                    }
                }

                is CommonEffect.PermissionMoveSettingAppEffect -> {
                    settingsPermission.value = effect.permissions
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}"))
                    launcherSettingApp.launch(intent)
                }

                /**
                 * Navigate
                 */
                is CommonEffect.Navigation.NavigateBack -> {
                    navController.popBackStack()
                }

                is CommonEffect.Navigation.NavigateApplicationExit -> {
                    (navController.context as? Activity)?.finish()
                }

                /**
                 * etc
                 */
                is CommonEffect.OpenWebBrowser -> {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    (navController.context as? Activity)?.startActivity(browserIntent)
                }

                is CommonEffect.RequestNetworkState -> {
                    val state = context.getSystemService(ConnectivityManager::class.java).run {
                        getNetworkCapabilities(activeNetwork).let { capabilities ->
                            if(capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
                                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                    NetworkState.TYPE_WIFI
                                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                    NetworkState.TYPE_MOBILE_DATA
                                } else {
                                    NetworkState.TYPE_ETC
                                }
                            }else{
                                NetworkState.TYPE_NOT_CONNECTED
                            }
                        }
                    }

                    onCommonEventSent(CommonEvent.SendNetworkState(state))
                }

                is CommonEffect.RequestInternetCheckExceptionEffect -> {

                    val isConnect = context.getSystemService(ConnectivityManager::class.java).run {
                        getNetworkCapabilities(activeNetwork).let { capabilities ->
                            (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_VALIDATED
                            ))
                        }
                    }
                    onCommonEventSent(
                        CommonEvent.RequestInternetExceptionResultEvent(
                            throwable =
                            if (isConnect) ApiUnknownException(result = effect.throwable.result)
                            else InternetDisconnectException(result = effect.throwable.result)
                        )
                    )
                }

                is CommonEffect.SendLogToEmailEffect -> {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        effect.logFile
                    )
                    val logMailIntent = Intent(Intent.ACTION_SEND).apply {
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_SUBJECT, "FancyMansion Log Data")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("ehdrnr1178@gmail.com" ))
                        putExtra(Intent.EXTRA_STREAM, uri)
                    }
                    context.startActivity(Intent.createChooser(logMailIntent,"FancyMansion"))
                }

                else -> {

                }
            }
        }.collect()
    }
}