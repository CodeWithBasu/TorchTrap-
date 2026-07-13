package com.example.torchtrap.ui.main

import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torchtrap.theme.*

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var isTorchOn by remember { mutableStateOf(false) }
    var showPrankDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val cameraManager = remember { context.getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    val cameraId = remember { 
        try { cameraManager.cameraIdList.firstOrNull { cameraManager.getCameraCharacteristics(it).get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true } } 
        catch (e: Exception) { null } 
    }

    // Effect to toggle physical torch
    LaunchedEffect(isTorchOn) {
        try {
            cameraId?.let { cameraManager.setTorchMode(it, isTorchOn) }
        } catch (e: Exception) {
            // Ignore if flashlight is unavailable (e.g. on emulator)
        }
    }

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
                        if (!isTorchOn) {
                            isTorchOn = true
                        } else {
                            // The Trap!
                            showPrankDialog = true
                        }
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
        
        if (showPrankDialog) {
            AlertDialog(
                onDismissRequest = { /* Disable tap-outside to dismiss for maximum prank effect */ },
                title = {
                    Text(text = "Payment Required!", color = PaymentRed, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(
                        text = "Pay ₹99 to turn off the torch.\n\n(Just kidding 😂)",
                        color = PureWhite,
                        fontSize = 16.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showPrankDialog = false
                            isTorchOn = false
                            Toast.makeText(context, "Gotcha!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                    ) {
                        Text("Pay Now", color = PureBlack, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = TorchOffGray,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
