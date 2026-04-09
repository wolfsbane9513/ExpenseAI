# ExpenseAI

ExpenseAI is an Android expense-tracking app built with Jetpack Compose, Hilt, Room, ML Kit OCR, and on-device Gemma support through MediaPipe LLM Inference. It is designed to capture expenses from receipt photos and shared text, then let the user review and confirm structured transactions before they are saved.

## What the app does

- Track expenses by month, category, and recent activity
- Scan paper receipts using the camera or gallery
- Extract receipt text with ML Kit OCR
- Stage shared receipt emails for review
- Stage shared transaction SMS text for review without requesting inbox-level SMS access
- Categorize expenses with heuristic fallback and optional on-device Gemma inference
- Store expenses locally with Room

## Current feature set

### Dashboard

- Monthly total spending
- Category totals
- Recent expenses
- On-device AI model status indicator
- In-app Gemma model install, replace, and removal controls

### Scan

- Camera capture with runtime camera permission handling
- Gallery import
- OCR text extraction
- Review and edit parsed expense data before saving

### Sources

- Shared email receipt intake
- Shared SMS transaction intake through Android share intents
- Pending review queue before confirm or reject

### History and Insights

- Expense list/history browsing
- AI-assisted spending insight generation with fallback behavior when no local model is installed

## Tech stack

- Kotlin
- Android Gradle Plugin 8.2.2
- Jetpack Compose
- Hilt
- Room
- ML Kit Text Recognition
- MediaPipe `tasks-genai`
- SQLCipher
- Detekt
- Android Lint
- GitHub Actions

## Requirements

- Android Studio with the bundled JBR / Java 17
- Android SDK 34
- Minimum Android version: API 26
- A connected emulator or Android device for runtime testing

## Project structure

```text
ExpenseAI/
|-- app/
|   |-- src/main/java/com/expenseai/
|   |   |-- ai/
|   |   |-- data/
|   |   |-- domain/
|   |   |-- security/
|   |   `-- ui/
|   `-- src/main/res/
|-- .github/workflows/
|-- config/
`-- docs/
```

## Build and run

From the repo root:

```bash
./gradlew assembleDebug
```

On Windows in this repo, builds have been run successfully with:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path='C:\Program Files\Android\Android Studio\jbr\bin;' + $env:Path
& 'C:\Program Files\Git\bin\bash.exe' ./gradlew assembleDebug
```

Install the debug APK with adb:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Useful local commands

```bash
./gradlew assembleDebug
./gradlew lintDebug
./gradlew detekt
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

## Emulator testing notes

The app has been smoke-tested on an Android emulator using adb-driven flows:

- Launch the app
- Open each bottom-navigation tab
- Verify camera permission prompt on first scan capture
- Verify shared email text stages as an `Email` pending item
- Verify shared SMS transaction text stages as an `SMS` pending item

Example explicit share-intent test:

```bash
adb shell am start -n com.expenseai.debug/com.expenseai.ShareActivity \
  --es android.intent.extra.SUBJECT Receipt \
  --es android.intent.extra.TEXT Total:INR349,merchant:Zomato,2026-04-09,Order:ZX20260409
```

## On-device Gemma setup

ExpenseAI supports on-device Gemma inference through MediaPipe LLM Inference.

### Supported local model bundle formats

- `.litertlm`
- `.task`
- `.bin`
- `.tflite`

### Where to place the model

The app looks for a local model file inside:

```text
<app files dir>/gemma_model/
```

Users can install a supported bundle directly from the dashboard. Today that install flow uses Android's document picker to bring the model into the app's private `gemma_model` directory, then immediately attempts to initialize MediaPipe inference.

The intended product direction is a hosted install experience:

- Surface an `Install AI Model` action in the app
- Download a signed model bundle from your own backend or CDN
- Verify checksum or signature before activation
- Store the bundle in app-private storage
- Reinitialize Gemma automatically so receipt parsing, SMS review, and insights can use the model

If no compatible model is present, the app falls back to deterministic parsing and categorization heuristics.

### Recommended direction

The current app code is aligned toward MediaPipe-compatible on-device model bundles rather than a hardcoded legacy Gemma binary path. For Android compatibility guidance, use Google's official MediaPipe LLM Inference documentation:

- [MediaPipe LLM Inference for Android](https://ai.google.dev/edge/mediapipe/solutions/genai/llm_inference/android)

## Security and privacy notes

- The app does not request broad photo-library access for normal receipt import
- The app does not request SMS inbox access for transaction parsing
- Shared SMS handling is user-initiated through Android share intents
- Pending transactions are reviewed before they become saved expenses
- Local model integrity checking is included for on-device model files

## CI

GitHub Actions workflows live in `.github/workflows/`.

Current pipeline areas include:

- Lint and static analysis
- Build
- Unit tests
- Instrumented tests
- Security scanning

The branch work in this repo includes a recent lint cleanup so `./gradlew lintDebug` passes locally again. CI is also configured to run on feature-branch pushes for earlier feedback before PR creation. The heavier jobs (`instrumented-tests` and `security-scan`) stay informational on feature-branch pushes, while remaining blocking on pull requests and protected branches.

## Known limitations

- No model weights are committed to the repository
- On-device Gemma performance depends heavily on the target hardware
- SMS ingestion is currently share-based, not inbox-sync based
- The emulator is useful for app-flow validation, but not for proving real Gemma inference support

## Contributing

1. Create a feature branch from the current default branch
2. Make focused commits
3. Run lint and the most relevant test task locally
4. Push the branch
5. Open a pull request

## License

No license file is currently checked into this repository.
