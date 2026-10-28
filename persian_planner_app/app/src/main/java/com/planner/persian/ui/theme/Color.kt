package com.planner.persian.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Indigo / Sky Accents
val PrimaryIndigo = Color(0xFF4F46E5)
val PrimaryIndigoDark = Color(0xFF6366F1)
val SecondarySky = Color(0xFF0EA5E9)
val SecondarySkyDark = Color(0xFF38BDF8)
val AccentTeal = Color(0xFF14B8A6)

// Priority Warm Status Colors
val PriorityHigh = Color(0xFFEF4444)
val PriorityHighContainer = Color(0xFFFEE2E2)
val PriorityHighText = Color(0xFF991B1B)

val PriorityMedium = Color(0xFFF59E0B)
val PriorityMediumContainer = Color(0xFFFEF3C7)
val PriorityMediumText = Color(0xFF92400E)

val PriorityLow = Color(0xFF10B981)
val PriorityLowContainer = Color(0xFFD1FAE5)
val PriorityLowText = Color(0xFF065F46)

// Soft Neutral Slate Backgrounds & Surfaces
val BackgroundLight = Color(0xFFF8F9FA)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF1F5F9)
val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF64748B)

val BackgroundDark = Color(0xFF121316)
val SurfaceDark = Color(0xFF1E2024)
val SurfaceVariantDark = Color(0xFF282B30)
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)

// Pastel Note Colors (Both Light and Dark friendly)
val PastelYellowLight = Color(0xFFFFF9C4)
val PastelYellowDark = Color(0xFF3E3A20)

val PastelBlueLight = Color(0xFFE1F5FE)
val PastelBlueDark = Color(0xFF1A3245)

val PastelGreenLight = Color(0xFFE8F5E9)
val PastelGreenDark = Color(0xFF1E3A29)

val PastelPinkLight = Color(0xFFFCE4EC)
val PastelPinkDark = Color(0xFF42212E)

val PastelPurpleLight = Color(0xFFEDE7F6)
val PastelPurpleDark = Color(0xFF2E2442)

val PastelOrangeLight = Color(0xFFFFF3E0)
val PastelOrangeDark = Color(0xFF422F1D)

val NoteColorPalette = listOf(
    "#FFF9C4" to "زرد ملایم",
    "#E1F5FE" to "آبی آسمانی",
    "#E8F5E9" to "سبز نعنایی",
    "#FCE4EC" to "صورتی گلبهی",
    "#EDE7F6" to "بنفش یاسی",
    "#FFF3E0" to "نارنجی کرم"
)

fun parseHexToColor(hex: String, isDark: Boolean): Color {
    return when (hex.uppercase()) {
        "#FFF9C4" -> if (isDark) PastelYellowDark else PastelYellowLight
        "#E1F5FE" -> if (isDark) PastelBlueDark else PastelBlueLight
        "#E8F5E9" -> if (isDark) PastelGreenDark else PastelGreenLight
        "#FCE4EC" -> if (isDark) PastelPinkDark else PastelPinkLight
        "#EDE7F6" -> if (isDark) PastelPurpleDark else PastelPurpleLight
        "#FFF3E0" -> if (isDark) PastelOrangeDark else PastelOrangeLight
        else -> try {
            Color(android.graphics.Color.parseColor(hex))
        } catch (e: Exception) {
            if (isDark) SurfaceDark else SurfaceLight
        }
    }
}
