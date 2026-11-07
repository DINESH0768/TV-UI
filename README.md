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
# Clean and assemble
./gradlew clean assembleDebug
```
The resulting APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### Install via ADB
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 7. Phase 1 Accomplishments
- [x] Initialized Gradle Kotlin DSL project with Gradle 8.8 and Android Gradle Plugin 8.4.1.
- [x] Configured Android TV `AndroidManifest.xml` with `LEANBACK_LAUNCHER`, `HOME`, `DEFAULT`, and `LAUNCHER` intent filters.
- [x] Implemented Dark Liquid Glass design system (palette `#090D16`, `#101827`, `#172235`, `#86B9FF`, `#65D6E8`).
- [x] Built TV-adapted Dynamic Island status capsule, oversized digital clock, live time/date ticker, and network status monitor.
- [x] Built horizontal installed apps launcher with background package query and leanback launch intent resolution.
- [x] Verified APK build and badging metadata (`com.aura.tv`, 6.75 MB).
