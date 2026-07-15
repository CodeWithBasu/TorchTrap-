<div align="center">

# 🔦 TorchTrap 
### The Ultimate Flashlight Prank

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
</p>

<p><strong>Because turning on a flashlight is easy... but turning it off shouldn't be. 😈</strong></p>

</div>

---

## 🎭 About The Project

**TorchTrap** is a brilliant, psychological prank application disguised as a sleek, premium, iOS-style flashlight utility for Android. 

At first glance, it functions perfectly—users tap a beautiful, haptic-feedback-enabled "ON" button, and their phone's camera flash illuminates the darkness. But when they try to turn it off? The trap springs.

What begins as a simple utility instantly devolves into a hilarious, terrifying gauntlet of fake paywalls, aggressive alarms, system crash simulations, and biometric scares, leaving your friends frantically trying to figure out how to escape without paying a ransom!

---

## 🗺️ The Trap Overflow Diagram

Here is exactly what happens to the victim once they fall into the trap:

```mermaid
graph TD
    A[🧑 Victim opens app] --> B(Taps 'ON' Button)
    B --> C{🔦 Flashlight turns on!}
    C --> D[Victim tries to turn it off]
    D --> E[Taps 'OFF' Button]
    D --> O[Pulls down System Control Panel]
    
    O -->|Intercepted! Forces Flashlight ON| F
    E --> F((🚨 TRAP ACTIVATED))
    
    F --> G[📸 Fake Face ID Capture]
    F --> H[🔊 Blares Error Alarm]
    F --> P[💓 Haptic Heartbeat Vibrations]
    
    G --> I[💬 Fake Bank SMS: '₹99.00 Debited']
    I --> J{Victim Panics}
    
    J -->|Swipes 'Back' to Escape| K[⬛ PITCH BLACK SCREEN]
    K --> L[⏳ Endless 'System Crash' Spinner]
    L --> M((☠️ Victim is trapped forever))
    
    style F fill:#ff0000,stroke:#333,stroke-width:4px,color:#fff
    style M fill:#000,stroke:#ff0000,stroke-width:2px,color:#fff
    style C fill:#f9d71c,stroke:#333,stroke-width:2px,color:#000
    style O fill:#333,stroke:#fff,stroke-width:2px,color:#fff
```

---

## ✨ Features 

| Feature | Description |
| :--- | :--- |
| **🍏 Premium Disguise** | A stunning, modern UI built with Jetpack Compose featuring custom fluid Claymorphism animations to lower the victim's guard. |
| **💸 The Ransom Paywall** | The "OFF" button is permanently locked behind a terrifying ₹99.00 payment demand. |
| **🚨 The System Buzzer** | Any attempt to interact with the locked button triggers a jarring, abrasive hardware alarm tone. |
| **💓 Haptic Heartbeat** | The phone's vibration motor physically mimics a slow, tense heartbeat while the victim stares at the ransom screen, building massive psychological tension. |
| **📸 Fake Biometrics** | The app flashes the screen bright white and fakes a facial recognition capture to simulate an unauthorized transaction. |
| **💬 Bogus Bank SMS** | A highly realistic system notification overlay drops down, convincing the user their bank account has just been debited. |
| **🛡️ System Override Intercept** | If the victim tries to cheat by pulling down their Android Control Panel to turn off the flashlight, the app instantly detects it, violently forces the flashlight back ON, and springs the trap anyway. |
| **💀 The Bricked OS Trap** | Intercepts the Android "Back" gesture, plunges the screen into absolute pitch black darkness, blares a fatal hardware intercept beep, and displays an endless loading ring. |

---

## 🚀 Installation

1. Navigate to the **[Releases](../../releases)** page of this repository.
2. Download the latest `app-debug.apk` file to your Android device.
3. Open the downloaded file and install the application *(you may need to allow "Install from Unknown Sources" in your Android settings)*.
4. Hand your phone to a friend and ask them to "use your flashlight for a second."

---

## 🛠️ Tech Stack

*   **Kotlin** - First-class and official programming language for Android development.
*   **Jetpack Compose** - Android's modern toolkit for building native UI, including custom fluid Claymorphism animations.
*   **Camera2 API** - For low-level hardware flashlight access and system state interception.
*   **ToneGenerator API** - For generating terrifying system-level error buzzers and alarms.
*   **Vibrator API** - For custom haptic heartbeat rhythm generation.

---

## ⚠️ Disclaimer

> [!WARNING]
> This application is strictly for educational, entertainment, and comedic purposes. It does **not** actually charge money, capture biometrics, access bank accounts, or crash operating systems. It is entirely a localized UI simulation designed for harmless pranks. 

<br>
<div align="center">
  <i>Have fun, and happy trapping! 🔦</i>
</div>