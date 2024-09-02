package com.fancymansion.core.presentation.compose.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val typographyTablet = Typography(
    // Display
    displayLarge = TextStyle(
        lineHeight = 37.8.sp,
        fontSize = 34.4.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    displayMedium = TextStyle(
        lineHeight = 34.3.sp,
        fontSize = 31.2.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    displaySmall = TextStyle(
        lineHeight = 32.6.sp,
        fontSize = 29.6.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),

    // Headline
    headlineLarge = TextStyle(
        lineHeight = 30.3.sp,
        fontSize = 27.5.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = TextStyle(
        lineHeight = 27.5.sp,
        fontSize = 25.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    headlineSmall = TextStyle(
        lineHeight = 26.1.sp,
        fontSize = 23.7.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),

    // Title
    titleLarge = TextStyle(
        lineHeight = 24.2.sp,
        fontSize = 22.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = TextStyle(
        lineHeight = 22.sp,
        fontSize = 20.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),
    titleSmall = TextStyle(
        lineHeight = 20.9.sp,
        fontSize = 19.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),

    // Body
    bodyLarge = TextStyle(
        lineHeight = 20.4.sp,
        fontSize = 17.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        lineHeight = 18.sp,
        fontSize = 15.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        lineHeight = 17.4.sp,
        fontSize = 14.5.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),

    // Label
    labelLarge = TextStyle(
        lineHeight = 19.2.sp,
        fontSize = 16.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    labelMedium = TextStyle(
        lineHeight = 16.9.sp,
        fontSize = 14.1.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    labelSmall = TextStyle(
        lineHeight = 16.3.sp,
        fontSize = 13.6.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    )
)