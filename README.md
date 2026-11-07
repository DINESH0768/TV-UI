# AURA TV — Premium Android TV Home Launcher

> **Tagline:** Your TV. Your Universe.
> **Package Name:** `com.aura.tv`
> **Design Philosophy:** Dark Liquid Glass, Cinematic UI, Minimalist Premium Experience.

---

## 1. Project Overview
AURA TV is a native Android TV home launcher designed specifically for large screens, 4K UHD displays, and memory-constrained TV hardware (2 GB RAM, Mali-450 GPU). It replaces the standard Android TV home screen with a dark, translucent glassmorphism interface inspired by cinematic cityscapes, oversized lock screen clocks, floating translucent capsules, and sleek streaming dashboards.

---

## 2. Target Device Specifications & Hardware Strategy
- **Operating System:** Android 14 (Target SDK 34, Min SDK 23)
- **Memory (RAM):** 2 GB
- **GPU:** ARM Mali-450 (Constrained graphics architecture)
- **CPU:** 4 Core
- **Internal Storage:** 16 GB
- **Target Display:** 4K UHD (3840 × 2160) & 1080p / 720p scalable densities

### GPU & Performance Optimizations
- **Simulated Glassmorphism:** Avoids expensive real-time Gaussian blurs that drop frames on Mali-450 GPUs. Uses translucent alpha drawables, multi-stop linear gradients, and subtle 1dp border highlights.
- **Hardware Acceleration:** Native hardware acceleration enabled with lightweight vector assets.
- **Memory Management:** Zero memory leaks, lifecycle-aware coroutines, single activity architecture, and background image/icon decoding.

---

## 3. Technology Stack & Architecture
- **Language:** Kotlin 1.9.24
- **Platform:** Native Android (compileSdk 34, targetSdk 34, minSdk 23)
- **UI Framework:** Android Views, ConstraintLayout, RecyclerView, ViewBinding
- **Architecture:** Clean MVVM (Model - View - ViewModel)
- **Async & Reactive:** Kotlin Coroutines & LiveData
- **Build System:** Gradle 8.8 with Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`)
- **Testing:** JUnit 4 unit tests & Android SDK `aapt` badging verification

---

## 4. Package Structure
```text
app/src/main/java/com/aura/tv/
├── core/
│   ├── config/
│   ├── navigation/
│   └── utils/
├── data/
│   └── apps/
│       └── AppRepository.kt
├── domain/
│   └── models/
│       └── InstalledApp.kt
└── ui/
    ├── MainActivity.kt
    └── home/
        ├── AppAdapter.kt
        └── HomeViewModel.kt
```

---

## 5. TV Remote Navigation & Focus States
AURA TV is fully operable using standard Android TV remote controls (no touchscreen required):
- **D-pad Navigation:** Smooth directional traversal between Header Settings, Dynamic Capsule, Featured Card, and App Grid.
- **Visual Focus Feedback:** Focused elements subtly scale up (1.05x - 1.08x) with a glowing `#86B9FF` border.
- **Key Handling:** Intercepts `KEYCODE_BACK` and `KEYCODE_HOME` to ensure the launcher remains steady without exiting to a blank screen.

---

## 6. Build & Installation Instructions

### Prerequisites
- JDK 17 or JDK 21
- Android SDK with Platform 34 and Build Tools 34.0.0+

### Build Debug APK
```bash
./gradlew clean assembleDebug
```
Output path: `app/build/outputs/apk/debug/app-debug.apk` (Size: ~6.75 MB)

### Build Release APK
```bash
./gradlew assembleRelease
```
Output path: `app/build/outputs/apk/release/app-release-unsigned.apk` (Size: ~5.32 MB)
*Note: The release APK is unsigned because production signing keys are kept private. It can be signed using `apksigner` with your release keystore.*

### Sign Release APK (Optional)
```bash
apksigner sign --ks <your-keystore.jks> --out app-release-signed.apk app/build/outputs/apk/release/app-release-unsigned.apk
```

### Install via ADB
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 7. Artifact Verification & Checksums
| Variant | File | Size | SHA-256 Checksum |
| :--- | :--- | :--- | :--- |
| **Debug** | `app-debug.apk` | 7,076,053 bytes (6.75 MB) | `54ae2ec722cce93b88242e5a2c0f08f2de5e09c14ed0fa677e6c99e9482cf205` |
| **Release (Unsigned)** | `app-release-unsigned.apk` | 5,581,604 bytes (5.32 MB) | `8d874fd52f1acc1c06a09247aac3b43fe79acea09904909b6cae442c650d8439` |

Both artifacts were inspected using `aapt dump badging`:
- `package: name='com.aura.tv'`
- `launchable-activity: name='com.aura.tv.ui.MainActivity'`
- `leanback-launchable-activity: name='com.aura.tv.ui.MainActivity'`
- `uses-feature-not-required: name='android.software.leanback'`
- `uses-feature-not-required: name='android.hardware.touchscreen'`
