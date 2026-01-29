package com.fastingtimerpro.app.ui.theme

import androidx.compose.ui.graphics.Color

object AppColors {

    // BACKGROUND
    val baseBackground = Color(0xFF0B1220)
    val gradientTop = Color(0xFF0F1A2E)
    val gradientBottom = Color(0xFF070B14)

    // ACCENT COLORS
    val primaryAccent = Color(0xFF4DA3FF)
    val secondaryAccent = Color(0xFF6FD3FF)

    // TEXT
    val primaryText = Color.White
    val secondaryText = Color(0xFFA9B4C7)
    val mutedText = Color(0xFF7C8698)

    // GLASS / CARDS
    val cardBackground = Color.White.copy(alpha = 0.08f)
    val cardBorder = Color.White.copy(alpha = 0.18f)

    // BUTTONS
    val primaryButtonBackground = Color(0xFF4DA3FF).copy(alpha = 0.25f)
    val secondaryButtonBackground = Color.White.copy(alpha = 0.10f)
    val destructiveButtonBackground = Color(0xFFFF5A5A).copy(alpha = 0.35f)

    // HIGHLIGHTS
    val brightRed = Color(0xFFFF5A5A)
}
