package com.example.torchtrap.ui.main

import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.torchtrap.theme.*

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var isTorchOn by remember { mutableStateOf(false) }
    var showPrankDialog by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val cameraManager = remember { context.getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    val cameraId = remember { 
        try { cameraManager.cameraIdList.firstOrNull { cameraManager.getCameraCharacteristics(it).get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true } } 
        catch (e: Exception) { null } 
    }

    LaunchedEffect(isTorchOn) {
        try {
            cameraId?.let { cameraManager.setTorchMode(it, isTorchOn) }
        } catch (e: Exception) {}
    }

    // Button Spring Animation (Squishy clay feel)
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else if (isTorchOn) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "scale"
    )

    // Smooth color transitions
    val buttonColor by animateColorAsState(
        targetValue = if (isTorchOn) ClaySurfaceOn else ClaySurfaceOff,
        animationSpec = tween(400), label = "color"
    )

    val textColor by animateColorAsState(
        targetValue = if (isTorchOn) TextLight else TextDark,
        animationSpec = tween(400), label = "textColor"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ClayBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TorchTrap",
                color = TextDark,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 80.dp)
            )

            // Claymorphism Toggle Button
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(buttonScale)
                    // Soft outer drop shadow
                    .shadow(
                        elevation = if (isPressed) 8.dp else 24.dp,
                        shape = CircleShape,
                        spotColor = if (isTorchOn) ClaySurfaceOn else ClayShadowDark,
                        ambientColor = if (isTorchOn) ClaySurfaceOn else ClayShadowDark
                    )
                    .clip(CircleShape)
                    .background(buttonColor)
                    // Inner Highlight (Top Left)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = if (isTorchOn) 0.3f else 0.8f), Color.Transparent),
                            center = Offset(100f, 100f),
                            radius = 300f
                        )
                    )
                    // Inner Shadow (Bottom Right)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color.Black.copy(alpha = if (isTorchOn) 0.15f else 0.05f), Color.Transparent),
                            center = Offset(500f, 500f),
                            radius = 400f
                        )
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                tryAwaitRelease()
                                isPressed = false
                                if (!isTorchOn) {
                                    isTorchOn = true
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                } else {
                                    showPrankDialog = true
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isTorchOn) "ON" else "OFF",
                    color = textColor,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Claymorphism Prank Dialog
        if (showPrankDialog) {
            Dialog(
                onDismissRequest = { /* Blocked */ },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(32.dp, RoundedCornerShape(32.dp), spotColor = ClayShadowDark, ambientColor = ClayShadowDark)
                        .clip(RoundedCornerShape(32.dp))
                        .background(ClaySurfaceOff)
                        // Inner Highlight
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color.White, Color.Transparent),
                                center = Offset(50f, 50f),
                                radius = 400f
                            )
                        )
                        // Inner Shadow
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.03f), Color.Transparent),
                                center = Offset(800f, 800f),
                                radius = 600f
                            )
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Soft 3D-like Icon representation
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .shadow(8.dp, CircleShape, spotColor = DangerClay)
                                .clip(CircleShape)
                                .background(DangerClay)
                                .background(Brush.radialGradient(listOf(Color.White.copy(0.4f), Color.Transparent), Offset(30f, 30f), 100f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "Payment Required",
                            color = TextDark,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Please pay ₹99 to turn off the torch. Just kidding! 😂",
                            color = TextDark.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Claymorphism Pay Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = ClaySurfaceOn)
                                .clip(RoundedCornerShape(24.dp))
                                .background(ClaySurfaceOn)
                                .background(Brush.radialGradient(listOf(Color.White.copy(0.3f), Color.Transparent), Offset(50f, 20f), 200f))
                                .clickable {
                                    showPrankDialog = false
                                    isTorchOn = false
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    Toast.makeText(context, "Gotcha! 🎈", Toast.LENGTH_LONG).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Pay ₹99", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
