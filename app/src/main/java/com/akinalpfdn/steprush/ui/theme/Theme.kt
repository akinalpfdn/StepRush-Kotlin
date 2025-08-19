package com.akinalpfdn.steprush.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 🌙 Dark theme (gelecekte kullanılabilir)
private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue,
    secondary = FreshGreen,
    tertiary = WarmOrange,
    background = TextPrimary,
    surface = TextSecondary,
    onPrimary = SurfaceWhite,
    onSecondary = SurfaceWhite,
    onTertiary = TextPrimary,
    onBackground = SurfaceWhite,
    onSurface = SurfaceWhite
)

// ☀️ Light theme - Ana tema (Neomorphic tasarım için optimize)
private val LightColorScheme = lightColorScheme(
    // Ana renkler
    primary = SkyBlue,           // Ana mavi - butonlar ve vurgular için
    secondary = FreshGreen,      // İkincil yeşil - success ve progress için
    tertiary = WarmOrange,       // Üçüncül turuncu - accent ve CTA'lar için
    
    // Arka plan ve yüzey renkleri
    background = BackgroundLight,    // Ultra açık mavimsi arka plan
    surface = SurfaceWhite,          // Kart ve yüzey rengi
    surfaceVariant = CardBackground, // Kart arka planı
    
    // Text renkleri (on: üzerindeki text rengi)
    onPrimary = SurfaceWhite,        // Ana renk üzerindeki text (beyaz)
    onSecondary = SurfaceWhite,      // İkincil renk üzerindeki text (beyaz)
    onTertiary = SurfaceWhite,       // Üçüncül renk üzerindeki text (beyaz)
    onBackground = TextPrimary,      // Arka plan üzerindeki text (koyu)
    onSurface = TextPrimary,         // Yüzey üzerindeki text (koyu)
    onSurfaceVariant = TextSecondary, // İkincil yüzey text (orta ton)
    
    // Durum renkleri
    error = ErrorRed,                // Hata rengi
    onError = SurfaceWhite,          // Hata rengi üzerindeki text
    
    // Border ve outline
    outline = TextLight,             // Çerçeve rengi (açık gri)
    outlineVariant = LightShadow     // İkincil çerçeve (çok açık)
)

@Composable
fun StepRushTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}