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
    // 컴포넌트  :
    headlineLarge = TextStyle(
        lineHeight = 30.3.sp,
        fontSize = 27.5.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),

    // 컴포넌트  : 탑바 타이틀 (태블릿)
    headlineMedium = TextStyle(
        lineHeight = 27.5.sp,
        fontSize = 25.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    // 컴포넌트  : 탑바 타이틀 (모바일)
    headlineSmall = TextStyle(
        lineHeight = 26.1.sp,
        fontSize = 23.7.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),

    // Title
    // 컴포넌트  : 라운드 버튼 (대)
    titleLarge = TextStyle(
        lineHeight = 24.2.sp,
        fontSize = 22.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),
    // 컴포넌트  : 서브 타이틀
    titleMedium = TextStyle(
        lineHeight = 22.sp,
        fontSize = 20.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),
    // 컴포넌트  : 라운드 버튼 (소)
    titleSmall = TextStyle(
        lineHeight = 20.9.sp,
        fontSize = 19.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),

    // Body
    // 컴포넌트  : 본문, 텍스트 필드, 메뉴 항목 텍스트
    bodyLarge = TextStyle(
        lineHeight = 20.4.sp,
        fontSize = 17.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  : 부연설명 (최대)
    bodyMedium = TextStyle(
        lineHeight = 18.sp,
        fontSize = 15.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  : 부연설명 (최소)
    bodySmall = TextStyle(
        lineHeight = 17.4.sp,
        fontSize = 14.5.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),

    // Label
    // 컴포넌트  : 체크박스
    labelLarge = TextStyle(
        lineHeight = 19.2.sp,
        fontSize = 16.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  :
    labelMedium = TextStyle(
        lineHeight = 16.9.sp,
        fontSize = 14.1.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  :
    labelSmall = TextStyle(
        lineHeight = 16.3.sp,
        fontSize = 13.6.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    )
)