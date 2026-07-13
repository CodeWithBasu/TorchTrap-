package com.example.torchtrap.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torchtrap.theme.*

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var isTorchOn by remember { mutableStateOf(false) }
    
    val buttonScale by animateFloatAsState(
        targetValue = if (isTorchOn) 1.1f else 1.0f,
        animationSpec = tween(300), label = "scale"
    )
    
    val buttonColor by animateColorAsState(
        targetValue = if (isTorchOn) NeonGreen else TorchOffGray,
        animationSpec = tween(300), label = "color"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TorchTrap",
                color = PureWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 64.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .scale(buttonScale)
                    .shadow(if (isTorchOn) 50.dp else 0.dp, CircleShape, ambientColor = NeonGreen, spotColor = NeonGreen)
                    .clip(CircleShape)
                    .background(buttonColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isTorchOn = !isTorchOn
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isTorchOn) "ON" else "OFF",
                    color = if (isTorchOn) PureBlack else PureWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
