package com.example.torchtrap.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // The fluid micro-animation squish
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.92f else 1f,
        label = "squish"
    )

    // A soft outer shadow for depth
    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isPressed && onClick != null) (elevation / 2) else elevation,
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
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null // Removes standard ripple so we just have the squish
                    ) { onClick() }
                } else Modifier
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
        modifier = modifier,
        onClick = onClick,
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
