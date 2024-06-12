package com.fancymansion.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.fancymansion.core.presentation.theme.typography.typographyMobile

val disableAlpha = 0.38f
val dividerAlpha = 0.38f
val dividerLightAlpha = 0.19f
val onTextAlpha = 0.76f

val subTextAlpha = 0.66f
val lightTextAlpha = 0.48f

val subWhiteTextAlpha = 0.72f

val dimmedAlpha = 0.28f
val dimmedBoldAlpha = 0.5f

const val coverHolderFrontAlpha = 0.9f
const val coverHolderCompleteAlpha = 0.3f

private val LightColorScheme = lightColorScheme(
    primary = ColorSet.blue_20b1f9,
    onPrimary = ColorSet.default_surface,
    primaryContainer = ColorSet.blue_20b1f9,
    onPrimaryContainer = ColorSet.gray_333333,
    inversePrimary = ColorSet.yellow_ffdd57,
    secondary = ColorSet.blue_618bff,
    onSecondary = ColorSet.default_surface,
    secondaryContainer = ColorSet.blue_3895ff,
    onSecondaryContainer = ColorSet.gray_333333,
    tertiary = ColorSet.blue_3370de,
    onTertiary = ColorSet.default_surface,
    tertiaryContainer = ColorSet.blue_20b1f9,
    onTertiaryContainer = ColorSet.gray_333333,

    surfaceTint = ColorSet.default_surface,

    background = ColorSet.gray_f9fafc,
    onBackground = ColorSet.gray_333333,

    inverseSurface = ColorSet.gray_333333,
    inverseOnSurface = ColorSet.default_surface,

    // state : normal
    surface = ColorSet.default_surface,
    onSurface = ColorSet.gray_333333,

    // state : not focus
    surfaceVariant = ColorSet.gray_fdfeff,
    onSurfaceVariant = ColorSet.gray_767b88,

    // state : disable
    // surfaceDisable = surface.copy(alpha = disableAlpha)
    // onSurfaceDisable = onSurface.copy(alpha = disableAlpha)

    // outline : normal
    outline = ColorSet.gray_333333,
    // outline : not focus
    outlineVariant = ColorSet.gray_a1b0c5,

    error = ColorSet.red_da3b2b,
    onError = ColorSet.default_surface,
    errorContainer = ColorSet.pink_faa39b,
    onErrorContainer = ColorSet.gray_333333
)

// Default 값은 MaterialTheme 에서 참조하고 Default가 아닌 경우 해당 객체에서 직접 참조
var colorScheme = LightColorScheme

@Composable
fun FancyMansionTheme(
    typography: Typography = typographyMobile,
    content : @Composable () -> Unit)
{
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography
    ){
        content()
    }
}