package com.cs467.onecalcplus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Note: Using system fonts as fallback if Space Grotesk and Manrope are not available in project resources.
// In a real project, these would be imported from Google Fonts or assets.
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif, // Should be Space Grotesk
        fontWeight = FontWeight.Bold,
        fontSize = 56.sp,
        lineHeight = 64.sp,
        letterSpacing = (-1.12).sp // -0.02em of 56sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Should be Space Grotesk
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Should be Manrope
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif, // Should be Manrope
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
