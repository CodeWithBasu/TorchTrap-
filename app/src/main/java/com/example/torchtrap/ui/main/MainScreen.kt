package com.example.torchtrap.ui.main

import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    val bgColor by animateColorAsState(if (isTorchOn) IosLightBackground else IosDarkBackground, tween(300))
    val textColor by animateColorAsState(if (isTorchOn) IosTextGray else IosTextGray, tween(300))
    val statusColor by animateColorAsState(if (isTorchOn) ClaySurfaceOn else Color.White, tween(300))
    
    val circleBgColor by animateColorAsState(if (isTorchOn) ClaySurfaceOn else IosButtonGray, tween(300))
    val iconColor by animateColorAsState(if (isTorchOn) Color.White else Color.White, tween(300))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            
            Text(
                text = "FLASHLIGHT",
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Giant Flashlight Circle
            Box(
                modifier = Modifier
                    .size(200.dp)
                    // If on, add a huge soft glow behind it
                    .background(
                        if (isTorchOn) Brush.radialGradient(
                            colors = listOf(ClaySurfaceOn.copy(alpha = 0.5f), Color.Transparent),
                            radius = 400f
                        ) else Brush.radialGradient(listOf(Color.Transparent, Color.Transparent))
                    )
                    .clip(CircleShape)
                    .background(circleBgColor)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (!isTorchOn) {
                                    isTorchOn = true
                                } else {
                                    showPrankDialog = true
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.FlashlightOn,
                    contentDescription = "Flashlight",
                    tint = iconColor,
                    modifier = Modifier.size(80.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = if (isTorchOn) "ON" else "OFF",
                color = statusColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 6.sp
            )
            
            Spacer(modifier = Modifier.weight(1.5f))
            
            // Bottom pill toggles
            Row(
                modifier = Modifier.padding(bottom = 60.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ON Button
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(if (isTorchOn) ClaySurfaceOn else IosDarkBackground)
                        .border(
                            width = 1.dp, 
                            color = if (!isTorchOn) IosButtonGray else Color.Transparent, 
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clickable {
                            if (!isTorchOn) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isTorchOn = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ON",
                        color = if (isTorchOn) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // OFF Button
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(if (isTorchOn) Color.Black else IosButtonGray)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (isTorchOn) {
                                showPrankDialog = true
                            }
                        }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "OFF",
                            color = if (isTorchOn) Color.White.copy(alpha = 0.5f) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    
                    // Tiny red lock badge in top right
                    if (isTorchOn) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 12.dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(IosLockRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Locked",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // iOS style modal for prank (Bottom Sheet style)
        if (showPrankDialog) {
            Dialog(
                onDismissRequest = { /* Blocked */ },
                properties = DialogProperties(
                    dismissOnBackPress = false, 
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(IosDarkGray)
                            .padding(top = 16.dp, bottom = 48.dp, start = 32.dp, end = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Handle
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(5.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.5f))
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Circular Icon Placeholder
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.3f))
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "FlashLight Pro",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Unlock the OFF Button",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$499",
                                color = Color.White,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = ".00",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Features List
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Turn off your flashlight", color = Color.White, fontSize = 14.sp)
                            Text("Lifetime darkness access", color = Color.White, fontSize = 14.sp)
                            Text("Priority OFF support", color = Color.White, fontSize = 14.sp)
                            Text("Exclusive black screen mode", color = Color.White, fontSize = 14.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        // Pay Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black)
                                .clickable {
                                    showPrankDialog = false
                                    isTorchOn = false
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    Toast.makeText(context, "Payment Accepted. Enjoy the darkness! 🎈", Toast.LENGTH_LONG).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pay $499.00", 
                                color = Color.White, 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 18.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // No Thanks text
                        Text(
                            text = "No thanks, I'll keep it on forever",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable {
                                showPrankDialog = false
                                // Note: isTorchOn remains TRUE!
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        )
                    }
                }
            }
        }
    }
}
