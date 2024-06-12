package com.fancymansion.core.common.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.fancymansion.core.common.log.Logger
import java.util.Locale
import kotlin.math.sqrt

fun getApiUserAgent(context: Context) : Pair<String, String>  {
    val app_name = "FANCY_MANSION_APP_AOS"
    val device_type = if(isTablet(context)) "tablet" else "phone"
    val app_version =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val device_name = Build.MODEL
    val os_name = "android"
    val os_version = Build.VERSION.RELEASE
    return "api-user-info" to "$app_name:$device_type/$app_version/$device_name/$os_name:$os_version"
}


fun secondToTimeFormat(second : Long) : String = String.format("%02d:%02d", second / 60, second % 60)

fun changeDateFormat(beforeDate:String?, beforeDelimiters:String, changeDelimiters:String) : String {
    if(beforeDate.isNullOrBlank()) return ""

    val splitDate = beforeDate.split(beforeDelimiters)
    return try {
        "${splitDate[0].toInt()}$changeDelimiters${splitDate[1].toInt()}$changeDelimiters${splitDate[2].toInt()}"
    }catch (e:Exception) {
        ""
    }
}

/**
 * string 체크하여 startChar, endChar에 들어가는 부분을 bold 처리하기 위해 html 형식으로 변경, 줄바꿈이 있다면 변경
 * ex) 가나다라 <마바사> 아자차 -> 마바사만 볼드 처리
 * HtmlCompat.fromHtml(text, 0).toAnnotatedString() -> 이렇게 text에 넣기
 * */
fun checkStringAndChangeBold(beforeString : String, startChar:Char = '<', endChar:Char = '>') : String {
    if(!beforeString.contains("<")){
        return  beforeString
    }
    var changeString = ""
    beforeString.forEach {
        changeString += when(it) {
            startChar -> "<b>"
            endChar -> "</b>"
            else -> it
        }
    }

    return  changeString.replace("\\n","<br>")
}


fun isTablet(context : Context) : Boolean {
    return checkTabletExceptionModel() ||
        checkIsTabletProperties() ||
        checkIsTabletInch(context) ||
        checkIsTabletSize(context)
}

private fun checkTabletExceptionModel(): Boolean {
    for (modelName in arrayOf(
        "SM-T385L",
        "SM-T380",
        "SM-T220",
        "iPlay_20",
        "iPlay_40",
        "Tab_Ultra",
        "MC401_GWL",
        "Z4PRO",
        "TB128FU",
        "TB371FC"
    )) {
        if (Build.MODEL == modelName) {
            Logger.i("TabletExceptionModel : ${Build.MODEL}")
            return true
        }
    }
    return false
}

private fun checkIsTabletProperties() : Boolean = try {
    val ism = Runtime.getRuntime().exec("getprop ro.build.characteristics").inputStream
    val bts = ByteArray(1024)
    ism.read(bts)
    ism.close()
    String(bts).lowercase(Locale.getDefault()).contains("tablet")
} catch (t: Throwable) {
    Logger.i("throwable : $t")
    false
}

private fun checkIsTabletInch(context: Context) : Boolean {
    val displayInch = context.resources.displayMetrics.let {
        val xInch = (it.widthPixels/it.xdpi).toDouble()
        val yInch = (it.heightPixels/it.ydpi).toDouble()
        sqrt((xInch*xInch) + (yInch*yInch))
    }
    Logger.i("displayInch : $displayInch")
    return displayInch > 7.0
}

private fun checkIsTabletSize(context: Context) : Boolean =
    context.resources.configuration.screenLayout.let { screenLayout ->
        Logger.i("screenLayout : $screenLayout, screenLayoutSize : ${screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK}")
        (screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE }