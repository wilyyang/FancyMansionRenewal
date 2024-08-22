package com.fancymansion.core.presentation.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.fancymansion.core.presentation.compose.theme.typography.typographyMobile

val disableAlpha = 0.38f
val dimmedAlpha = 0.28f

const val CONTENT_DIMMED_ALPHA = 0.56f

const val ON_CONTENT_DEACTIVE_ALPHA = 0.392f
const val ON_CONTENT_SUB_ALPHA = 0.478f
const val ON_CONTENT_DIMMED_ALPHA = 0.95f

private val LightColorScheme = lightColorScheme(
    primary = ColorSet.blue_1e9eff,
    onPrimary = ColorSet.white_ffffff,
    primaryContainer = ColorSet.sky_8fc5f2,
    onPrimaryContainer = ColorSet.white_ffffff,

    secondary = ColorSet.cyan_1ecdcd,
    onSecondary = ColorSet.white_ffffff,

    tertiary = ColorSet.navy_324155,
    onTertiary = ColorSet.white_ffffff,

    background = ColorSet.gray_f3f4f5,
    onBackground = ColorSet.black_303538,

    surface = ColorSet.white_ffffff,
    onSurface = ColorSet.black_333333,
    surfaceVariant = ColorSet.white_f7fafc,
    onSurfaceVariant = ColorSet.black_353a3d,

    surfaceContainer = ColorSet.gray_f2f4f5,

    outline = ColorSet.gray_e7e9ec,
    outlineVariant = ColorSet.gray_d7dbde
)

val onBackgroundDimmed = ColorSet.gray_9ea7ad
val onBackgroundInactive = ColorSet.gray_9ea7ad
val onBackgroundSub = ColorSet.gray_a4aab0

val onSurfaceDimmed = ColorSet.gray_787878
val onSurfaceInactive = ColorSet.gray_787878
val onSurfaceSub = ColorSet.gray_a4aab0

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