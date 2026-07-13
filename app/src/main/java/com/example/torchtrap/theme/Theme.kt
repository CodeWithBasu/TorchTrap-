package com.example.torchtrap.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    background = DarkBackground,
    surface = DarkBackground,
    onPrimary = PureBlack,
    onBackground = PureWhite,
    onSurface = PureWhite
)

@Composable
fun TorchTrapTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
