package com.example.torchtrap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ClayBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF0F0F3),
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    // A soft outer shadow for depth
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            // The magic: An inner glow/shadow using a linear gradient border to fake 3D volume
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.9f), // Top-left highlight
                        Color.Black.copy(alpha = 0.05f) // Bottom-right subtle shading
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ClayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF0F0F3),
    textColor: Color = Color.Black
) {
    ClayBox(
        modifier = modifier.clickable { onClick() },
        backgroundColor = backgroundColor,
        cornerRadius = 32.dp,
        elevation = 6.dp
    ) {
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
            Text(
                text = text,
                color = textColor,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}
