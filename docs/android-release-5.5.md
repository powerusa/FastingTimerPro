# Android 5.5 (versionCode 10)

- Fix in-app language selection using AppCompatActivity.
- Follow the system language when the override is cleared.
- Handle system Back from Settings and History.
- Localize planned fasting duration labels.
- Move signing credentials from build.gradle.kts to ignored keystore.properties.

## Build
Use JDK 17 (tested with OpenJDK 17.0.20), Android SDK 36 and the Gradle wrapper.
Create android/FastingTimerPro/keystore.properties locally with storeFile, storePassword, keyAlias and keyPassword. Use the existing upload key. Do not commit this file or the key.

Run: ./gradlew :app:testDebugUnitTest :app:connectedDebugAndroidTest :app:bundleRelease :app:lintDebug
An Android emulator or connected device is required for connected tests.

## Validation
5 unit tests and 2 instrumentation tests passed on Pixel 9 / Android 16 (API 36).
Debug lint and release vital lint passed with no errors; nonblocking warnings remain.
The release AAB signature was verified with jarsigner.
Older Android language selection still requires device verification; no older runtime is installed locally.

## English release notes
Improved language selection and switching back to the device language.
Fixed Android Back navigation from Settings and History.
Localized fasting duration labels.
