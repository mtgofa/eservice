package com.mtgofa.eservice.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtgofa.eservice.R

object Palette {
    val Blue = Color(0xFF1E40AF)
    val BlueDark = Color(0xFF172F7E)
    val BlueSoft = Color(0xFFE3EBFF)
    val Teal = Color(0xFF0E9F8E)
    val TealSoft = Color(0xFFDDF5F1)
    val Bg = Color(0xFFF4F6FB)
    val Surface = Color(0xFFFFFFFF)
    val Ink = Color(0xFF151A2D)
    val Muted = Color(0xFF667085)
    val Line = Color(0xFFE4E7EE)
}

val Plex = FontFamily(
    Font(R.font.plexar_regular, FontWeight.Normal),
    Font(R.font.plexar_semibold, FontWeight.SemiBold),
    Font(R.font.plexar_bold, FontWeight.Bold),
)

private val base = TextStyle(fontFamily = Plex)

private val AppTypography = Typography(
    displaySmall = base.copy(fontSize = 30.sp, fontWeight = FontWeight.Bold),
    headlineMedium = base.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
    headlineSmall = base.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
    titleLarge = base.copy(fontSize = 19.sp, fontWeight = FontWeight.Bold),
    titleMedium = base.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    titleSmall = base.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = base.copy(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = base.copy(fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall = base.copy(fontSize = 12.sp, lineHeight = 17.sp),
    labelLarge = base.copy(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = base.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
    labelSmall = base.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
)

private val LightColors = lightColorScheme(
    primary = Palette.Blue,
    onPrimary = Color.White,
    primaryContainer = Palette.BlueSoft,
    onPrimaryContainer = Palette.BlueDark,
    secondary = Palette.Teal,
    onSecondary = Color.White,
    secondaryContainer = Palette.TealSoft,
    onSecondaryContainer = Color(0xFF0B4F47),
    background = Palette.Bg,
    onBackground = Palette.Ink,
    surface = Palette.Surface,
    onSurface = Palette.Ink,
    surfaceVariant = Color(0xFFEEF1F7),
    onSurfaceVariant = Palette.Muted,
    surfaceContainer = Palette.Surface,
    surfaceContainerHigh = Palette.Surface,
    surfaceContainerHighest = Color(0xFFEEF1F7),
    surfaceContainerLow = Palette.Surface,
    outline = Color(0xFFC9CFDB),
    outlineVariant = Palette.Line,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9DB6FF),
    onPrimary = Color(0xFF0D2264),
    primaryContainer = Color(0xFF233A8C),
    onPrimaryContainer = Color(0xFFDCE4FF),
    secondary = Color(0xFF6FD8CA),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF0B4F47),
    onSecondaryContainer = Palette.TealSoft,
    background = Color(0xFF0F1220),
    onBackground = Color(0xFFE4E6F0),
    surface = Color(0xFF181C2C),
    onSurface = Color(0xFFE4E6F0),
    surfaceVariant = Color(0xFF242A3D),
    onSurfaceVariant = Color(0xFFA6ADC2),
    surfaceContainer = Color(0xFF181C2C),
    surfaceContainerHigh = Color(0xFF1E2335),
    surfaceContainerHighest = Color(0xFF242A3D),
    surfaceContainerLow = Color(0xFF181C2C),
    outline = Color(0xFF4A5269),
    outlineVariant = Color(0xFF2C3247),
)

@Composable
fun EServiceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = AppTypography,
        shapes = Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp),
        ),
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, content = content)
    }
}
