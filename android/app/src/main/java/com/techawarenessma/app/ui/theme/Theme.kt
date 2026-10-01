package com.techawarenessma.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.techawarenessma.app.R

/** The website's palette, named by role rather than hex. */
object TaaColors {
    val Cream = Color(0xFFF8F3F1)
    val Sand = Color(0xFFE7E1DE)
    val Ink = Color(0xFF181818)
    val Slate = Color(0xFF2C3E50)
    val Red = Color(0xFFD22B42)
    val Yellow = Color(0xFFFAC206)
    val Cyan = Color(0xFF16C0FF)
    val Coral = Color(0xFFFB4B5F)
    val Blue = Color(0xFF0B7DA8)
    val Espresso = Color(0xFF2E2B25)
    val Night = Color(0xFF0C0C0E)
    val Gold = Color(0xFFA87B00)
    val GoldDeep = Color(0xFF946D00)

    /** Yellow is too light for text on cream; this is the site's darkened stand-in. */
    val GoldText = Color(0xFF8F6B00)

    /** oklch(0.52 0.13 245) from the Pro Mat widget, converted to sRGB. */
    val MatBlue = Color(0xFF036EAE)
}

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold),
)

/** Type scale mirroring the site's rem sizes, adjusted for phone widths. */
object TaaType {
    val Hero = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 50.sp, lineHeight = 50.sp, letterSpacing = (-1).sp)
    val PageTitle = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 44.sp, lineHeight = 44.sp, letterSpacing = (-0.8).sp)
    val Section = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, lineHeight = 36.sp, letterSpacing = (-0.6).sp)
    val Subsection = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, lineHeight = 30.sp, letterSpacing = (-0.3).sp)
    val CardTitle = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp)
    val Title = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 24.sp)
    val BodyLarge = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 28.sp)
    val Body = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 24.sp)
    val Small = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp)
    val Button = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp)
    val Eyebrow = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 18.sp, letterSpacing = 2.sp)
    val Label = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 1.sp)
    val Caption = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 1.1.sp)
    /** Tabular figures keep count-up digits from jittering, like the site's mono numerals. */
    val Stat = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 40.sp, lineHeight = 46.sp, letterSpacing = (-0.8).sp, fontFeatureSettings = "tnum")
    val BigNumber = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp, lineHeight = 40.sp)
}

private val DefaultTypography = Typography()

private val PoppinsTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = Poppins),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = Poppins),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = Poppins),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = Poppins),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = Poppins),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = Poppins),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = Poppins),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = Poppins),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = Poppins),
    // Poppins is already wide; Material's default tracking makes field labels look stretched.
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = Poppins, letterSpacing = 0.sp),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = Poppins, letterSpacing = 0.sp),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = Poppins, letterSpacing = 0.sp),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = Poppins, letterSpacing = 0.sp),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = Poppins, fontWeight = FontWeight.SemiBold, letterSpacing = 0.sp),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = Poppins, letterSpacing = 0.sp),
)

private val TaaColorScheme = lightColorScheme(
    primary = TaaColors.Ink,
    onPrimary = TaaColors.Cream,
    secondary = TaaColors.Red,
    onSecondary = TaaColors.Cream,
    tertiary = TaaColors.Blue,
    background = TaaColors.Cream,
    onBackground = TaaColors.Slate,
    surface = TaaColors.Cream,
    onSurface = TaaColors.Slate,
    surfaceVariant = TaaColors.Sand,
    onSurfaceVariant = TaaColors.Slate,
    surfaceContainer = TaaColors.Cream,
    secondaryContainer = TaaColors.Yellow,
    onSecondaryContainer = TaaColors.Ink,
    error = TaaColors.Red,
    outline = TaaColors.Slate,
)

/**
 * The website has a single light theme, so the app does too: the brand colors are the
 * point, and the dark sections of each page already give it contrast.
 */
@Composable
fun TaaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = TaaColorScheme, typography = PoppinsTypography, content = content)
}
