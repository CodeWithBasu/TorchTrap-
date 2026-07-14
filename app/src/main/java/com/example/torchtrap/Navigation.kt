package com.example.torchtrap

import android.content.Context
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.torchtrap.ui.main.MainScreen
import com.example.torchtrap.ui.main.OnboardingScreen

@Composable
fun MainNavigation() {
  val context = LocalContext.current
  val hasSeenOnboarding = remember {
      val sharedPref = context.getSharedPreferences("TorchTrapPrefs", Context.MODE_PRIVATE)
      sharedPref.getBoolean("has_seen_onboarding", false)
  }

  val backStack = rememberNavBackStack(if (hasSeenOnboarding) Main else Onboarding)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Main> {
          MainScreen(modifier = Modifier.safeDrawingPadding())
        }
        entry<Onboarding> {
          OnboardingScreen(
              onGetStarted = {
                  backStack.removeLastOrNull()
                  backStack.add(Main)
              },
              modifier = Modifier.safeDrawingPadding()
          )
        }
      },
  )
}
