package com.fancymansion.core.presentation.compose.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fancymansion.core.presentation.R

val pretendard = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal)
)

val typographyMobile = Typography(
    // Display
    displayLarge = TextStyle(
        lineHeight = 26.8.sp,
        fontSize = 24.4.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    displayMedium = TextStyle(
        lineHeight = 24.1.sp,
        fontSize = 21.9.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    displaySmall = TextStyle(
        lineHeight = 22.9.sp,
        fontSize = 20.8.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),

    // Headline
    // 컴포넌트  :
    headlineLarge = TextStyle(
        lineHeight = 23.3.sp,
        fontSize = 21.2.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),

    // 컴포넌트  : 탑바 타이틀 (태블릿)
    headlineMedium = TextStyle(
        lineHeight = 21.0.sp,
        fontSize = 19.1.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),
    // 컴포넌트  : 탑바 타이틀 (모바일)
    headlineSmall = TextStyle(
        lineHeight = 19.9.sp,
        fontSize = 18.1.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold
    ),

    // Title
    // 컴포넌트  : 라운드 버튼 (대)
    titleLarge = TextStyle(
        lineHeight = 22.2.sp,
        fontSize = 20.2.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),
    // 컴포넌트  : 서브 타이틀
    titleMedium = TextStyle(
        lineHeight = 18.7.sp,
        fontSize = 17.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),
    // 컴포넌트  : 라운드 버튼 (소)
    titleSmall = TextStyle(
        lineHeight = 17.8.sp,
        fontSize = 16.2.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold
    ),

    // Body
    // 컴포넌트  : 본문, 텍스트 필드, 메뉴 항목 텍스트
    bodyLarge = TextStyle(
        lineHeight = 18.sp,
        fontSize = 15.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  : 부연설명 (최대)
    bodyMedium = TextStyle(
        lineHeight = 16.8.sp,
        fontSize = 14.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  : 부연설명 (최소)
    bodySmall = TextStyle(
        lineHeight = 15.6.sp,
        fontSize = 13.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),

    // Label
    // 컴포넌트  : 체크박스
    labelLarge = TextStyle(
        lineHeight = 15.6.sp,
        fontSize = 13.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  :
    labelMedium = TextStyle(
        lineHeight = 13.sp,
        fontSize = 10.8.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    ),
    // 컴포넌트  :
    labelSmall = TextStyle(
        lineHeight = 8.8.sp,
        fontSize = 7.3.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    )
)