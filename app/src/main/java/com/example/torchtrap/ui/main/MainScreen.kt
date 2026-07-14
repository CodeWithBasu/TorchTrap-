package com.example.torchtrap.ui.main

import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

    // Dynamic Immersive Background
    val bgCenterColor by animateColorAsState(
        if (isTorchOn) Color(0xFF1E3A2F) else Color(0xFF121212), tween(800), label = "bg"
    )
    val bgOuterColor = Color(0xFF070707)

    // Breathing effect for the ON state
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow_radius"
    )

    // Button Spring Animation
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else if (isTorchOn) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(bgCenterColor, bgOuterColor), radius = 1500f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TORCH TRAP",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 20.sp,
                letterSpacing = 8.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 120.dp)
            )

            // The Immersive Toggle Button
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .scale(buttonScale)
                    .shadow(if (isTorchOn) glowRadius.dp else 10.dp, CircleShape, spotColor = if (isTorchOn) GlowGreen else Color.Black)
                    .clip(CircleShape)
                    .background(
                        if (isTorchOn) Brush.linearGradient(listOf(GlowGreen, GlowCyan)) 
                        else Brush.linearGradient(listOf(Color(0xFF2A2A2A), Color(0xFF1A1A1A)))
                    )
                    .border(2.dp, if (isTorchOn) Color.White.copy(alpha = 0.5f) else Color(0xFF333333), CircleShape)
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
                // Inner bezel
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0F0F0F))
                        .border(1.dp, Color(0xFF222222), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isTorchOn) "ACTIVE" else "STANDBY",
                        color = if (isTorchOn) GlowGreen else Color.Gray,
                        fontSize = 18.sp,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Custom Immersive Prank Dialog
        if (showPrankDialog) {
            Dialog(
                onDismissRequest = { /* Blocked */ },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xEE111111))
                        .border(1.dp, DangerRed.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "⚠️",
                            fontSize = 48.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "SYSTEM LOCKED",
                            color = DangerRed,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Premium flashlight feature requires immediate payment of ₹99 to disengage hardware.",
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Fake Pay Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.horizontalGradient(listOf(DangerRed, Color(0xFFFF5555))))
                                .clickable {
                                    showPrankDialog = false
                                    isTorchOn = false
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    Toast.makeText(context, "Payment processing... Just kidding! 😂", Toast.LENGTH_LONG).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("AUTHORIZE PAYMENT", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }
    }
}
