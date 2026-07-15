# FIRE OS (formerly ExpenseAI)

FIRE OS is an on-device, privacy-first personal finance app for the **Financial
Independence, Retire Early (FIRE)** community. It turns everyday transactions
(SMS, email, receipts) into a live wealth projection and answers the one question
that matters: **"what does this decision cost me in FIRE days?"**

All parsing, categorization, and projection run **locally** — no data leaves the
device. Optional on-device Gemma inference handles the fuzzy parts; a pure-Kotlin
engine does the math.

## Key Features

- **FIRE War Room** (Dashboard) — live view of corpus, net worth, and progress toward
  financial independence.
- **Projection engine** (Projections) — month-by-month simulation of your path to a
  target corpus, with phase breakdowns, a net-worth breakdown at the target date, and
  an honest "reframe" when the target isn't reachable on time.
- **Intelligence hub** (Sources) — SMS, email, and receipt parsing staged as pending
  expenses for one-tap review, deduplicated on ingest.
- **FIRE-days impact** — model a one-time purchase and see how many days it pushes back
  your FIRE date.
- **Privacy first** — local-only Room database, biometric vault lock, encrypted
  preferences, and on-device inference.

## The FIRE Engine

A dependency-free Kotlin engine (`domain/fire/`) simulates the corpus forward over a
configurable horizon (default 360 months):

- **Target corpus** — 25× annual retirement expenses, or an explicit target.
- **Monthly cashflow** — income streams (with annual growth), recurring outflows, loan
  EMIs (fully-amortizing), and one-time inflow/outflow events.
- **Growth** — equity CAGR applied monthly (SIP convention); declining-balance loan
  outstanding for net-worth accuracy.
- **Outputs** — FIRE date, full trajectory, auto-derived phases, net-worth breakdown
  (liquid corpus + non-corpus assets + property − loans), and reframe options
  (extend the date / lower the target / add income).

Core math lives in `FireMath.kt`; simulation in `FireEngine.kt`; domain types in
`FireModel.kt` / `FireResult.kt`. The engine has regression tests, including a
"War Room" scenario derived from real financial documents.

## Tech Stack

- **UI** — Jetpack Compose (Material 3), dark-only fintech palette
- **Architecture** — Clean Architecture (data / domain / ui) with Hilt DI
- **Persistence** — Room (v3) with GSON serialization for FIRE model storage
- **Intelligence** — MediaPipe LLM Inference running Gemma (on-device), with
  rule-based fallbacks; ML Kit OCR for receipts
- **Security** — Android Biometrics + Encrypted SharedPreferences; input sanitizer
  for prompt-injection defense; root/debugger/emulator detection

## Navigation

Five bottom-nav tabs plus Settings:

| Tab | Screen | Purpose |
|-----|--------|---------|
| War Room | Dashboard | Live FIRE snapshot |
| Pulse Scan | Scan | Camera + OCR receipt capture |
| Log | History | Transaction history |
| Projections | Insights | Engine output & trajectory |
| Intelligence | Sources | SMS/email/receipt staging & review |
| Settings | FireModel | Edit FIRE profile & assumptions |

## Project Structure

```
app/src/main/java/com/expenseai/
├── ai/            Gemma service, SMS/email parsers, OCR, prompt templates
├── data/          Room DB, DAOs, repositories (expenses + FIRE model)
├── domain/
│   ├── fire/      FIRE engine, math, model, result types
│   └── usecase/   Process SMS/email/receipt, categorize, insights
├── security/      Biometric, encrypted prefs, sanitizer, integrity checks
└── ui/            Compose screens, components, navigation, theme
```

Specs and plans live under `docs/superpowers/`.

## Build & Run

Standard Android/Gradle project. `minSdk 26`, `targetSdk 34`, Java 17.

```bash
./gradlew assembleDebug          # build
./gradlew testDebugUnitTest      # engine + parser unit tests
./gradlew connectedDebugAndroidTest   # instrumented tests (device/emulator)
```

The Gemma model file must be placed on-device (imported via the in-app flow into
`filesDir/gemma_model/`); the app degrades to rule-based parsing when absent.

## Privacy

`allowBackup="false"`, `usesCleartextTraffic="false"`, biometric lock, and encrypted
storage. No network calls for core functionality — projections and parsing are fully
on-device.

---
*Note: the package name `com.expenseai` is retained for stability and migration consistency.*
