# ExpenseAI: SMS/Email Data Sources, MediaPipe Wiring & Testing Design

**Date:** 2026-04-06  
**Status:** Approved  
**Scope:** Three deliverables — (1) wire MediaPipe LlmInference, (2) SMS + email parsing data sources, (3) comprehensive test suite  

---

## 1. Goals

- Complete the `runInference()` stub in `GemmaService` so on-device AI actually runs
- Add SMS inbox scanning as an automatic expense detection source
- Add email parsing via Android Share Intent (privacy-preserving, no OAuth)
- Gate all auto-detected expenses behind a user-review step before writing to the database
- Introduce a full test suite (unit + integration + UI)
- Refresh the visual design to a Material 3 fintech palette with OLED dark mode

---

## 2. Non-Goals

- Gmail API / OAuth email integration (breaks privacy-first model)
- Cloud-based AI inference (all processing stays on-device)
- Automatic background SMS polling (scan triggers on app open only)
- Modifying or migrating existing expense data

---

## 3. Architecture

### 3.1 MediaPipe LlmInference Wiring

**File:** `app/src/main/java/com/expenseai/ai/GemmaService.kt`

`GemmaService` gains a `LlmInference?` field. `initialize()` constructs it via:

```kotlin
LlmInference.createFromOptions(
    context,
    LlmInference.LlmInferenceOptions.builder()
        .setModelPath(modelManager.getModelPath()!!)
        .setMaxTokens(1024)
        .setTemperature(0.1f)   // low temp for structured extraction
        .setTopK(1)
        .build()
)
```

`runInference(prompt)` calls `llmInference!!.generateResponse(prompt)`. The existing fallback chain (rule-based) is unchanged — it activates whenever `isInitialized` is false or inference throws.

**No architecture changes.** One field + two method edits.

---

### 3.2 SMS Data Source

#### New files

| File | Purpose |
|------|---------|
| `ai/SmsReader.kt` | Queries `content://sms/inbox` via ContentResolver, returns raw SMS list from the last 30 days |
| `ai/SmsParser.kt` | Regex engine for Indian bank transaction SMS patterns; returns `ParsedReceipt` |
| `domain/usecase/ProcessSmsUseCase.kt` | Orchestrates: read → filter → parse → stage as pending |
| `data/local/PendingExpenseEntity.kt` | Room entity for staged (unconfirmed) expenses |
| `data/local/PendingExpenseDao.kt` | CRUD for the `pending_expenses` table |
| `ui/screens/sources/SourcesScreen.kt` | 5th bottom nav tab |
| `ui/screens/sources/SourcesViewModel.kt` | Drives SourcesScreen state |
| `ui/screens/review/PendingReviewSheet.kt` | Bottom sheet — confirm / edit / reject pending expenses |
| `ui/screens/review/ReviewViewModel.kt` | Drives PendingReviewSheet state |

#### SmsParser regex patterns (Indian banks)

The parser recognises these message structures, in priority order:

1. **Debit alert** — `debited.*Rs\.?\s*(\d+[\d,]*)` (HDFC, SBI, Axis)
2. **Credit card spend** — `spent.*INR\s*(\d+[\d,]*)` (ICICI, Kotak)
3. **UPI debit** — `UPI.*debited.*₹(\d+[\d,]*)` (PhonePe, GPay forwards)
4. **ATM withdrawal** — `ATM.*withdrawn.*Rs\.?\s*(\d+[\d,]*)`
5. **Net banking** — `transferred.*Rs\.?\s*(\d+[\d,]*).*to\s+(.+)`

Each match also extracts:
- **Vendor/payee** — text after `to`, `at`, `merchant`, or `VPA`
- **Date** — `dd-MM-yy` or `dd/MM/yyyy` patterns within the message
- **Reference number** — for deduplication

Messages that match none of the patterns are silently skipped (not all SMS is financial).

#### SMS deduplication

`PendingExpenseEntity` stores the SMS reference number (or a hash of body+date+amount if no reference). Before staging, the use case checks both `pending_expenses` and `expenses` tables for an existing match — duplicate messages are skipped silently.

#### Permission handling

`READ_SMS` is requested at runtime on first toggle of the SMS source in `SourcesScreen`. If denied, the toggle reverts and a Snackbar explains why. The manifest declares the permission but the feature is fully disabled unless granted.

---

### 3.3 Email Data Source (Share Intent)

#### Approach: `ACTION_SEND` Share Target

No new permissions. The user selects **Share → ExpenseAI** from any email app. Android delivers the email body as `text/plain` extras.

#### New files

| File | Purpose |
|------|---------|
| `ai/EmailParser.kt` | Extracts amount, vendor, date from email text; handles HTML-stripped plain text |
| `domain/usecase/ProcessEmailUseCase.kt` | Receives shared text → parses → shows PendingReviewSheet |
| `ui/ShareActivity.kt` | Transparent activity; handles `ACTION_SEND`, calls `ProcessEmailUseCase` to stage the pending expense, then navigates into `PendingReviewSheet` for the single item |

#### EmailParser patterns

Transactional emails share common structures regardless of sender:

1. **Amount** — `(?:Rs\.?|INR|₹)\s*([\d,]+(?:\.\d{1,2})?)` or `(?:Total|Amount|Grand Total)[:\s]*(?:Rs\.?|INR|₹)?\s*([\d,]+)`
2. **Vendor** — subject line extraction; `(?:from|at|merchant)[:\s]+([A-Za-z0-9 &]+)`
3. **Date** — ISO (`2026-04-06`), Indian (`06 Apr 2026`), or slash (`06/04/2026`) formats
4. **Order ID** — `(?:Order|Booking|Ref)[:\s#]+([A-Z0-9\-]+)` (used for deduplication)

If Gemma is available, ambiguous emails go through `GemmaService.parseReceipt()` (reuses existing method with the email body as input).

#### Manifest addition

```xml
<activity
    android:name=".ui.ShareActivity"
    android:exported="true"
    android:theme="@style/Theme.ExpenseAI.Translucent"  <!-- define in themes.xml: windowIsTranslucent=true, windowBackground=@android:color/transparent -->
    android:excludeFromRecents="true">
    <intent-filter>
        <action android:name="android.intent.action.SEND" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="text/plain" />
    </intent-filter>
</activity>
```

---

### 3.4 Pending Review Flow

All auto-detected expenses (SMS or email) land in `pending_expenses` — never directly in `expenses`.

**PendingReviewSheet** presents each pending item as a card with:
- Pre-filled vendor, amount, category, date (editable inline)
- Source badge: `SMS` or `Email`
- **Confirm** (saves to `expenses`, deletes from `pending_expenses`)
- **Edit then Confirm** (opens full edit form)
- **Reject** (deletes from `pending_expenses`)

The sheet supports swipe-to-dismiss for reject, following Material 3 bottom sheet conventions.

**`SourcesScreen`** shows:
- SMS toggle (with `READ_SMS` permission gating)
- Email source indicator (always on — no permission needed)
- "X pending review" chip → taps open `PendingReviewSheet`
- Last SMS scan timestamp

---

### 3.5 InputSanitizer Extensions

`security/InputSanitizer.kt` gains two new methods:

```kotlin
fun sanitizeSmsText(raw: String): String  // strips control chars, limits to 500 chars
fun sanitizeEmailText(raw: String): String // strips HTML tags, limits to 2000 chars
```

Both follow the same prompt-injection prevention pattern as the existing `sanitizeOcrText()`.

---

### 3.6 Navigation Change

`NavGraph.kt` adds a 5th bottom nav entry:

```kotlin
data object Sources : Screen("sources", "Sources", Icons.Default.Source)
```

Badge count on the Sources tab reflects `pendingExpenseCount` from `PendingExpenseDao`.

---

## 4. UI / Design Language

### 4.1 Color Palette (Material 3 — applied via `Theme.kt`)

| Token | Value | Use |
|-------|-------|-----|
| Primary | `#059669` | FAB, active nav, confirm buttons |
| On Primary | `#FFFFFF` | |
| Secondary | `#10B981` | Progress bars, category chips |
| Error / Accent | `#DC2626` | Reject, overspend warnings |
| Background | `#0F172A` | OLED dark (dark theme) |
| Surface | `#1E293B` | Cards, bottom sheet |
| Surface Variant | `#272F42` | Input fields, muted cards |
| On Surface | `#F8FAFC` | Primary text |
| Muted | `#94A3B8` | Secondary text, timestamps |
| Border | `#334155` | Dividers |

Light theme uses the same primary green with `#F8FAFC` background and `#0F172A` text.

### 4.2 Typography

**Font:** Plus Jakarta Sans via Android Downloadable Fonts (Google Fonts provider, declared in `res/font/plus_jakarta_sans.xml`). Falls back to system sans-serif if unavailable.  
Weights used: 400 (body), 500 (labels), 600 (titles), 700 (display amounts)

Applied via Material 3 type roles:
- `displaySmall` → 700, spending totals
- `titleMedium` → 600, section headers
- `bodyMedium` → 400, expense rows
- `labelSmall` → 500, source badges, timestamps

### 4.3 Source Badge Component (`CategoryChip.kt` extension)

Small pill rendered on `ExpenseCard`:

| Source | Label | Color |
|--------|-------|-------|
| `manual` | Manual | `#94A3B8` (muted) |
| `scan` | Scan | `#3B82F6` (blue) |
| `sms` | SMS | `#8B5CF6` (purple) |
| `email` | Email | `#F59E0B` (amber) |

### 4.4 Key UX Rules (from ui-ux-pro-max audit)

- All touch targets ≥ 48dp
- Skeleton shimmer (not spinner) during SMS scan — scan typically takes 1–3 seconds
- Snackbar with "Undo" for rejected pending expenses (3-second window)
- `prefers-reduced-motion` respected (no skeleton animation if system reduces motion)
- Biometric auth prompt shown once per session when user opens `SourcesScreen` for the first time (not on every tab visit); uses existing `BiometricHelper`
- Bottom sheet max height = 75% screen height; scrollable if list exceeds

---

## 5. Testing Strategy

### 5.1 New Dependencies (add to `app/build.gradle.kts`)

```kotlin
// Unit testing
testImplementation("io.mockk:mockk:1.13.10")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("app.cash.turbine:turbine:1.1.0")
testImplementation("androidx.room:room-testing:2.6.1")

// Hilt testing
androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
kspAndroidTest("com.google.dagger:hilt-compiler:2.50")
```

### 5.2 Unit Tests

#### `SmsParserTest.kt`
Tests 20+ real-world Indian bank SMS fixtures across all 5 regex patterns:
- HDFC debit alert, ICICI credit card spend, Axis UPI, SBI ATM, Kotak net banking
- Edge cases: amounts with commas (`1,500.00`), missing vendor, missing date
- Verifies deduplication key generation is stable

#### `EmailParserTest.kt`
Tests against fixture email bodies:
- Amazon order confirmation, Swiggy receipt, IRCTC ticket, hotel booking
- HTML-stripped plain text variants
- Partial matches (amount but no date → date defaults to today)

#### `GemmaServiceTest.kt`
- Fallback path: `isInitialized = false` → `fallbackParseReceipt()` returns correct structure
- Fallback categorizer: all 8 category keyword groups
- `InputSanitizer` integration: prompt injection strings are neutralised before reaching parser

#### `DashboardViewModelTest.kt`
Uses Turbine to assert StateFlow emissions:
- Initial load emits `isLoading = true` then `isLoading = false`
- `addExpense()` causes `recentExpenses` to update
- Month navigation updates `currentMonth` correctly

#### `InsightsViewModelTest.kt`
- `generateInsights()` with no model → fallback insights string contains total amount

### 5.3 Integration Tests (Room in-memory)

#### `ExpenseRepositoryTest.kt`
```kotlin
@Before fun setup() {
    db = Room.inMemoryDatabaseBuilder(context, ExpenseDatabase::class.java)
        .allowMainThreadQueries().build()
}
```
Tests:
- Insert → query by month → correct results
- Category totals sum correctly
- Delete removes from all queries
- `PendingExpenseDao` confirm flow (pending → expenses, pending deleted)

### 5.4 UI Tests (Compose)

#### `DashboardScreenTest.kt`
- Empty state: "No expenses yet" card visible
- After insert: expense card renders vendor name and amount
- FAB click: Add Expense dialog opens
- Model status indicator shows correct icon per `ModelStatus`

#### `SourcesScreenTest.kt`
- SMS toggle off by default
- Toggle prompts permission (mocked)
- "X pending" chip visible when pending count > 0

#### `PendingReviewSheetTest.kt`
- Confirm button moves expense to confirmed list
- Reject button removes item from list
- Swipe-to-reject gesture works

---

## 6. File Change Summary

### New files (22)
```
app/src/main/java/com/expenseai/ai/SmsReader.kt
app/src/main/java/com/expenseai/ai/SmsParser.kt
app/src/main/java/com/expenseai/ai/EmailParser.kt
app/src/main/java/com/expenseai/domain/usecase/ProcessSmsUseCase.kt
app/src/main/java/com/expenseai/domain/usecase/ProcessEmailUseCase.kt
app/src/main/java/com/expenseai/data/local/PendingExpenseEntity.kt
app/src/main/java/com/expenseai/data/local/PendingExpenseDao.kt
app/src/main/java/com/expenseai/ui/screens/sources/SourcesScreen.kt
app/src/main/java/com/expenseai/ui/screens/sources/SourcesViewModel.kt
app/src/main/java/com/expenseai/ui/screens/review/PendingReviewSheet.kt
app/src/main/java/com/expenseai/ui/screens/review/ReviewViewModel.kt
app/src/main/java/com/expenseai/ui/ShareActivity.kt
app/src/test/java/com/expenseai/SmsParserTest.kt
app/src/test/java/com/expenseai/EmailParserTest.kt
app/src/test/java/com/expenseai/GemmaServiceTest.kt
app/src/test/java/com/expenseai/DashboardViewModelTest.kt
app/src/test/java/com/expenseai/InsightsViewModelTest.kt
app/src/test/java/com/expenseai/ExpenseRepositoryTest.kt
app/src/androidTest/java/com/expenseai/DashboardScreenTest.kt
app/src/androidTest/java/com/expenseai/SourcesScreenTest.kt
app/src/androidTest/java/com/expenseai/PendingReviewSheetTest.kt
app/src/main/res/font/plus_jakarta_sans.xml
```

### Modified files (8)
```
app/src/main/java/com/expenseai/ai/GemmaService.kt         ← wire LlmInference
app/src/main/java/com/expenseai/ai/PromptTemplates.kt      ← add SMS + email prompts
app/src/main/java/com/expenseai/data/local/ExpenseDatabase.kt ← add PendingExpenseDao
app/src/main/java/com/expenseai/di/AppModule.kt            ← provide PendingExpenseDao
app/src/main/java/com/expenseai/security/InputSanitizer.kt ← add SMS/email sanitizers
app/src/main/java/com/expenseai/ui/navigation/NavGraph.kt  ← add Sources tab + badge
app/src/main/java/com/expenseai/ui/theme/Theme.kt          ← apply fintech color palette
app/src/main/AndroidManifest.xml                           ← READ_SMS + ShareActivity
```

---

## 7. Out-of-Scope (Future)

- Periodic background SMS scan (WorkManager job) — deferred; app-open scan is sufficient for v1
- WhatsApp payment message parsing — different structure, separate spec
- Multi-currency support — currently hardcoded to INR
- ML-based SMS classifier to replace regex — would need training data collection first
