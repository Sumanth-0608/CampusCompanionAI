# Implementation Plan - Fix Black Screen Hang

The app is launching to a black screen due to a **Main Thread Hang (ANR)** during the initial composition. This is caused by a combination of hallucinated/futuristic dependency versions and incompatible Jetpack Compose syntax that triggers a runtime verification failure.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Suman/AndroidStudioProjects/CampusCompanionAI/gradle/libs.versions.toml)
* Downgrade `agp` from `9.4.0` to `8.5.0` (Stable).
* Downgrade `kotlin` from `2.2.10` to `2.0.0` (Stable).
* Downgrade `composeBom` from `2026.02.01` to `2024.05.00` (Stable).
* Update `ksp` plugin to match the Kotlin version.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/Suman/AndroidStudioProjects/CampusCompanionAI/app/build.gradle.kts)
* Fix `compileSdk` and `targetSdk` to use standard versions (API 35).
* Downgrade Room compiler to `2.6.1` to match the runtime version and ensure stability.

### UI Components

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Suman/AndroidStudioProjects/CampusCompanionAI/app/src/main/java/com/sumanth/campuscompanionai/MainActivity.kt)
* **Wrap content in `Surface`**: Ensures a theme-compliant background and prevents potential "black-on-black" rendering issues.
* **Fix `LinearProgressIndicator`**: Change `progress = { ... }` (lambda) to `progress = attendancePercentage / 100f` (Float). The lambda syntax is experimental/newer and is triggering a bytecode verification hang with the current mismatched compiler.
* **Fix `ProfileScreen` String Formatting**: Correct the `Text` call to actually format the CGPA instead of displaying the code as a string literal.

## Verification Plan

### Automated Tests
* Run `gradlew assembleDebug` to ensure the project compiles with stable versions.

### Manual Verification
* Launch the app on the emulator and verify that the Dashboard is rendered correctly.
* Navigate between screens to ensure state management and rendering are working as expected.
