# Login App (HCM Portal)

A modern Android application built with Jetpack Compose, Material 3, and real Google Sign-In authentication.

## 🚀 How to Run on Windows (Windows 7, 8.1, 10, and 11)

### Option 1: Download from GitHub Releases / Actions (Fastest)
1. Go to your GitHub repository's **Releases** or **Actions** tab.
2. Download the `login-app-debug-apk` artifact.
3. On Windows:
   - **Windows 11**: Open directly with **Windows Subsystem for Android (WSA)**.
   - **Windows 7, 8, 10, 11**: Drag and drop the `.apk` into **BlueStacks**, **NoxPlayer**, or **LDPlayer**.

---

### Option 2: Build & Run via Android Studio on Windows
1. Clone this repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
   ```
2. Open the cloned folder in **Android Studio** on your Windows PC.
3. Click **Run (`Shift + F10`)** to launch it on an emulator or connected device.
4. To build the standalone release APK, run:
   ```bash
   ./gradlew assembleRelease
   ```

---

### Option 3: Compile as Desktop Native Executable (.exe)
Since the UI is built with Kotlin Jetpack Compose, it can be packaged as a Windows desktop application using Compose Multiplatform:
```bash
./gradlew packageExe
```
The output installer will be located in `build/compose/binaries/main/exe/`.

---

## 🛠️ Tech Stack
- **UI Framework:** Jetpack Compose (Material 3)
- **Language:** Kotlin 2.0
- **Auth:** Real Google Sign-In & Firebase Auth
- **Build System:** Gradle (Kotlin DSL)
