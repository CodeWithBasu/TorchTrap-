package com.example.torchtrap.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = IosYellow,
    background = IosLightBackground,
    surface = IosLightBackground,
    onPrimary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun TorchTrapTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = LightColorScheme, typography = Typography, content = content)
}
