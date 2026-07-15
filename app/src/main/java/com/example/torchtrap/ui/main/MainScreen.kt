package com.example.torchtrap.ui.main

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.ToneGenerator
import android.media.AudioManager
import kotlinx.coroutines.delay
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.torchtrap.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
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
import com.example.torchtrap.ui.components.ClayBox

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var isTorchOn by remember { mutableStateOf(false) }
    var showPrankDialog by remember { mutableStateOf(false) }
    var isProcessingPayment by remember { mutableStateOf(false) }
    var showFakeSms by remember { mutableStateOf(false) }
    var showFormattingTrap by remember { mutableStateOf(false) }
    var formatProgress by remember { mutableFloatStateOf(0f) }
    var showCameraFlash by remember { mutableStateOf(false) }
    var showFaceAuthMessage by remember { mutableStateOf(false) }
    var showSystemCrash by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
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

    // Funny buzzer sound effect and terrifying heartbeat when trap springs
    LaunchedEffect(showPrankDialog) {
        if (showPrankDialog) {
            val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneGenerator.startTone(ToneGenerator.TONE_SUP_ERROR, 600) // Loud annoying buzzer
            delay(800)
            toneGenerator.release()
            
            // Start the terrifying heartbeat loop while they stare at the paywall
            while (showPrankDialog && !isProcessingPayment) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                delay(150)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                delay(1200) // Wait for next beat, slow and tense
            }
        }
    }
    
    // Prevent back button from escaping the trap, trigger the system crash instead!
    BackHandler(enabled = isTorchOn || showPrankDialog || showSystemCrash) {
        if (showPrankDialog) {
            // Close the dialog but instantly trigger the fake OS crash
            showPrankDialog = false
            showSystemCrash = true
            
            // Play a terrifying abrupt error sound
            val tg = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 1000)
        }
    }

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
                        .clickable { 
                            if (!isTorchOn) {
                                isTorchOn = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            } else {
                                // Trigger the camera flash and face auth prank!
                                scope.launch {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    
                                    // Camera shutter sound
                                    val tg = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                                    tg.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                                    
                                    // Flash the screen
                                    showCameraFlash = true
                                    delay(100)
                                    showCameraFlash = false
                                    
                                    // Show Face Auth message
                                    showFaceAuthMessage = true
                                    delay(1500)
                                    showFaceAuthMessage = false
                                    
                                    // Finally, show the payment trap
                                    showPrankDialog = true
                                }
                            }
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
                ClayBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(56.dp),
                    backgroundColor = if (isTorchOn) ClaySurfaceOn else IosDarkBackground,
                    cornerRadius = 28.dp,
                    elevation = if (isTorchOn) 2.dp else 8.dp,
                    onClick = {
                        if (!isTorchOn) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isTorchOn = true
                        }
                    }
                ) {
                    Text(
                        text = "ON",
                        color = if (isTorchOn) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // OFF Button
                ClayBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(56.dp),
                    backgroundColor = if (isTorchOn) Color.Black else IosButtonGray,
                    cornerRadius = 28.dp,
                    elevation = if (isTorchOn) 8.dp else 2.dp,
                    onClick = {
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
                        
                        // Eager People Avatars
                        Image(
                            painter = painterResource(id = R.drawable.people_avatars),
                            contentDescription = "Eager People",
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(100.dp)
                                .clip(RoundedCornerShape(16.dp))
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
                                text = "₹99",
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
                                    if (!isProcessingPayment) {
                                        scope.launch {
                                            isProcessingPayment = true
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            // Wait 3 agonizing seconds
                                            delay(3000)
                                            
                                            isProcessingPayment = false
                                            showPrankDialog = false
                                            isTorchOn = false
                                            // Success sound
                                            val tg = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                                            tg.startTone(ToneGenerator.TONE_PROP_BEEP2, 200)
                                            
                                            // Show terrifying SMS
                                            showFakeSms = true
                                            delay(5000)
                                            showFakeSms = false
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isProcessingPayment) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Pay ₹99.00", 
                                    color = Color.White, 
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 18.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // No Thanks text
                        Text(
                            text = "No thanks, I'll keep it on forever",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    showPrankDialog = false
                                    // Trigger the terrifying formatting trap!
                                    showFormattingTrap = true
                                    formatProgress = 0f
                                    
                                    // Blare the alarm immediately!
                                    val tg = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                                    tg.startTone(ToneGenerator.TONE_SUP_ERROR, 5000) 
                                    
                                    // Animate the fake progress
                                    while (formatProgress < 1f) {
                                        delay(300)
                                        formatProgress += kotlin.random.Random.nextDouble(0.05, 0.15).toFloat() // Jump randomly
                                        if (formatProgress > 1f) formatProgress = 1f
                                    }
                                    
                                    // Hide trap after a few seconds of 100%
                                    delay(2000)
                                    showFormattingTrap = false
                                    
                                    // Show SMS anyway to freak them out!
                                    showFakeSms = true
                                    delay(5000)
                                    showFakeSms = false
                                }
                            }
                        )
                    }
                }
            }
        }

        // Fake SMS Notification Overlay
        AnimatedVisibility(
            visible = showFakeSms,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2C2C2E).copy(alpha = 0.95f))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF007AFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💬", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Messages", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        "Bank Alert: Rs. 99.00 has been debited from your A/c XXXX. Ref: TXN-847291.", 
                        color = Color.LightGray, 
                        fontSize = 13.sp, 
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // The Evil "Formatting Phone..." Trap Overlay
        AnimatedVisibility(
            visible = showFormattingTrap,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(500))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD32F2F)), // Terrifying Red
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    // Giant warning icon
                    Text(text = "⚠️", fontSize = 80.sp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "UNAUTHORIZED CANCELLATION DETECTED",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Factory Resetting Phone to Protect Data...",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // Progress Bar
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { formatProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = Color.White,
                        trackColor = Color.Black.copy(alpha = 0.3f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "${(formatProgress * 100).toInt()}% Complete",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
        
        // Fake Camera Flash
        AnimatedVisibility(
            visible = showCameraFlash,
            enter = fadeIn(tween(50)),
            exit = fadeOut(tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }
        
        // Face Auth Message
        AnimatedVisibility(
            visible = showFaceAuthMessage,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📸", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Face Captured",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Authorizing Payment via Bank...",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        }
        
        // The Infinite "System Crash" Loop
        AnimatedVisibility(
            visible = showSystemCrash,
            enter = fadeIn(tween(0)), // Instant black screen
            exit = fadeOut(tween(300)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black), // Pitch black dead screen
                contentAlignment = Alignment.Center
            ) {
                // Tiny spinning white circle like a phone booting or crashing
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}
