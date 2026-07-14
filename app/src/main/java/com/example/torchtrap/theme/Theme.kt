package com.example.torchtrap.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ClaySurfaceOn,
    background = ClayBackground,
    surface = ClaySurfaceOff,
    onPrimary = TextLight,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun TorchTrapTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = LightColorScheme, typography = Typography, content = content)
}
