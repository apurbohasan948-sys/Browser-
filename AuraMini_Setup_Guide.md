# AuraMini AI Browser - Architectural Blueprints & Launch SOP
This handbook compiles the exact system configurations, local synchronization instructions, and deployment runbooks required to publish **AuraMini AI Browser** to the Google Play Console sandbox.

---

## Part 1: High-Performance Architecture Blueprint

AuraMini implements a highly decoupeable, modular **MVVM (Model-View-ViewModel)** system built entirely within Jetpack Compose + Room Local Cache + Retrofit direct endpoints:

```
+-----------------------------------------------------------+
|                        UI Layer                           |
|  [AuraBrowserMain] <-> [DashboardViews] <-> [WebView]     |
+-----------------------------------------------------------+
                              |
                              v
+-----------------------------------------------------------+
|                     State Management                      |
|                  [BrowserViewModel]                       |
+-----------------------------------------------------------+
                              |
                              v
+-----------------------------------------------------------+
|                         Data Hub                          |
|                  [BrowserRepository]                      |
|       +---------------------+-----------------------+     |
|       |                     |                       |     |
|       v                     v                       v     |
|  [BrowserDatabase]   [RetrofitClient]       [AdBlock Engine]
|  (Room / SQLite)      (Gemini Direct)        (DNS & Resource)
+-----------------------------------------------------------+
```


### Technical Specifications & Built-In Optimizations
1. **Adaptive Decompression Mode (Lite-Mode)**: Custom `WebViewClient` interceptor filters resource requests, blocking image networks entirely to preserve up to **90% bandwidth usage**.
2. **Deterministic AdBlocker Engine**: Intercepts structural resources before binding, filtering tracking, telemetry, and ad script origins to boost speed.
3. **Smart Dark Mode Injection**: Modulates stylesheet structures of loaded pages by compounding invert filters and color attributes inside the secure WebView sandbox.
4. **Holographic Avatar Assist**: Draws rotating rendering components using Jetpack Compose drawing arcs and transitions to display AI thinking phases securely.

---

## Part 2: Step-by-Step Setup Guide

Follow these steps to customize, compile, and run AuraMini locally or on your preferred test handsets:

### Step 1: Secure API Configuration
AuraMini accesses the Gemini LLM engine directly through the REST API layer. You MUST configure this key securely inside the Google AI Studio cloud parameters:
1. In the **Google AI Studio** dashboard, navigate to the **Secrets panel**.
2. Click **Create Secret** or locate the `GEMINI_API_KEY` parameter.
3. Bind your real, active Gemini API Key to this parameter. The build system will automatically inject it into the `.env` property and compile it directly inside `BuildConfig.GEMINI_API_KEY`.

> ⚠️ **Key Extraction Warning**: Android APKs can be easily decompiled. If distributing public demos containing BuildConfig variables, avoid attaching production credit accounts to protect against reverse-engineering.

### Step 2: Build Requirements
- **JDK Target**: Java Development Kit 17+ compiles modern Compose and Kotlin configurations.
- **Gradle Version**: 8.2+ with target Android Gradle Plugin compatible bindings.
- **KSP Processing**: Ensure Kotlin Symbol Processing is used for Room compiler generation.

---

## Part 3: App Publishing Guide for Google Play Store

Follow this checklists to publish your modern browser to Google Play Console:

### 1. Developer Console Checklist
1. Enroll in the [Google Play Console](https://play.google.com/console) with your developer account credentials.
2. Click **Create app** on the top dashboard.
3. Name your app (e.g., `AuraMini AI Browser`), choose the default language, and select **App** under categorization metrics.

### 2. Standard Cryptographic Signing Configuration
Google requires all release builds to contain unique cryptographic signatures:
1. Generate your secure release Certificate Keystore from Android Studio:
   - Navigate to **Build > Generate Signed Bundle / APK...**
   - Choose **Android App Bundle (AAB)** (which automatically optimizes split assets based on handset parameters).
   - Click **Create new...** to create a custom keystore path, establishing a strong, non-lossy credentials password.
   - Set the cryptographic Alias to `upload` or similar, saving this file securely.
2. Inject your production secrets into your machine's global environment variables to prevent hardcoding files in `.gradle` records:
   - `KEYSTORE_PATH` -> Path of your `.jks` file
   - `STORE_PASSWORD` -> Keystore overall password
   - `KEY_PASSWORD` -> Cryptographic alias key password

### 3. Core App Policies & Declaration (MANDATORY for Browsers)
Since AuraMini is a web browser rendering external URLs, Google Play Console enforces strict security policies before approval:
1. **Is your app a Web Browser?** Yes. You MUST declare that your application is a dedicated browser client that is fully compliant with the **Google Safe Browsing API** or built on standard optimized WebView clients with secure sandbox features.
2. **Age Rating / Content Rating**: Complete the interactive questionnaire. Since users can open any URL, select the appropriate flags indicating that your browser accesses general interest content.
3. **Data Safety Declarations**:
   - Declare that AuraMini does NOT collect user-sensitive data in incognito mode.
   - Declare that standard credentials passwords stored in the Room database are localized on the local handset using sandboxed SQL databases, adhering to modern privacy regulations.
4. **Malware and Safe Shield**: Ensure standard adblocker criteria don't violate affiliate policies. The built-in URL filters must focus exclusively on safe surfing, malware, and privacy controls.

### 4. Deploying the AAB Asset
1. Once compilation finishes, navigate to the `/app/build/outputs/bundle/release/` directory to locate the generated `.aab` production file.
2. Upload this asset to the **Production track** inside Google Play Console.
3. Customize your Store Presence using high-fidelity graphic assets, screenshot views, description details, and promotional materials.
4. Click **Start roll-out to Production** to initiate official review!
