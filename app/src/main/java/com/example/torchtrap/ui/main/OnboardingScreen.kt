package com.example.torchtrap.ui.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.torchtrap.R
import com.example.torchtrap.theme.*

@Composable
fun OnboardingScreen(onGetStarted: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF67B5FF)), // Light blue top background matching reference
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top 3D Image Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.3f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.clay_torch),
                contentDescription = "Torch 3D Illustration",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentScale = ContentScale.Fit
            )
        }
        
        // Bottom White Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Small pager indicator (visual only for the mockup)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.LightGray))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.height(6.dp).width(16.dp).clip(RoundedCornerShape(3.dp)).background(ClaySurfaceOn))
                }
                
                Text(
                    text = "Light up your\nworld today",
                    color = TextDark,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "A simple, fun, and extremely\nbright flashlight tool for your smartphone",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Claymorphism Get Started Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = ClaySurfaceOn)
                        .clip(RoundedCornerShape(24.dp))
                        .background(ClaySurfaceOn)
                        .background(Brush.radialGradient(listOf(Color.White.copy(0.3f), Color.Transparent), Offset(50f, 20f), 200f))
                        .clickable {
                            // Save to shared preferences that onboarding is done
                            val sharedPref = context.getSharedPreferences("TorchTrapPrefs", Context.MODE_PRIVATE)
                            with (sharedPref.edit()) {
                                putBoolean("has_seen_onboarding", true)
                                apply()
                            }
                            onGetStarted()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Get started", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}
