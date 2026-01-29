# Fasting Timer VIP Pro

This repo contains two native apps:

- ios/ — Swift + SwiftUI
- android/ — Kotlin + Jetpack Compose

This repo also contains generated source code for the core fasting engine + an initial dashboard UI.

## Project creation (native templates)

### iOS (Xcode)

1. Open Xcode.
2. File → New → Project…
3. iOS → App
4. Product Name: `FastingTimerPro`
5. Interface: SwiftUI
6. Language: Swift
7. Bundle Identifier suggestion: `com.fastingtimerpro.app` (or your own)
8. Save the project into: `ios/`

After creation:

1. Copy all `.swift` files from `ios/FastingTimerPro/Sources/` into your Xcode project.
2. In your Xcode-generated `@main App` file, set the root view to:

   `FastingTimerProRootView()`

### Android (Android Studio)

1. Open Android Studio.
2. New Project → Phone and Tablet → Empty Activity
3. Name: `FastingTimerPro`
4. Package name suggestion: `com.fastingtimerpro.app` (or your own)
5. Language: Kotlin
6. Minimum SDK: 26+ recommended
7. Use Jetpack Compose: enabled
8. Save the project into: `android/`

After creation:

1. Copy the Kotlin sources from `android/FastingTimerPro/app/src/main/java/` into your Android Studio project under the same package.
2. Copy `AndroidManifest.xml` and `res/values/` resources from `android/FastingTimerPro/app/src/main/`.
3. Ensure your app module has these dependencies:

   - Jetpack Compose (Material3)
   - Navigation Compose
   - Coroutines
   - (Optional next step) Room + DataStore

## Privacy Policy

Privacy Policy URL for App Store Connect:  
**https://powerusa.github.io/FastingTimerPro/privacy-policy**

To enable GitHub Pages:
1. Go to your repo: https://github.com/powerusa/FastingTimerPro
2. Settings → Pages
3. Source: Deploy from a branch
4. Branch: `main`, folder: `/docs`
5. Save

## Next implementation steps

After the native shells exist, we’ll implement:

- Fasting session engine (single active fast, retroactive start, extend)
- Stage timeline computation (supports durations beyond 72 hours)
- Local persistence (iOS: CoreData; Android: Room)
- Dashboard + timeline UI (glass / frosted cards)
- Local notifications (stage transitions ON by default; hydration OFF by default)
