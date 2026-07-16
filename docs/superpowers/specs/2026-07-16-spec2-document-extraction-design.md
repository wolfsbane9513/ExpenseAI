# Spec 2: On-Device Document Extraction — Design

**Status:** Approved (brainstormed 2026-07-16)
**Depends on:** Spec 1 (FIRE engine + FireModel), Spec 1.5 (scenarios; established FireRepository read-modify-write patterns)

## 1. Goal

Import a financial PDF (salary slip or loan statement), extract the fields the FIRE
projection needs, and let the user confirm-and-correct them before they merge into the
`FireModel`. Everything runs **on-device** — no network, no cloud, no API keys.

> **Decision revision:** the original pivot plan (2026-05-22) proposed a hybrid
> on-device + opt-in cloud (Claude Haiku) extraction. On 2026-07-16 the user chose
> **on-device only, for privacy**. The cloud path is dropped from scope; the
> `DocumentParser` seam is where a cloud extractor would slot in later if ever wanted.

## 2. Scope

**In (v1):**
- Salary slips → `IncomeStream` (employer label, monthly net pay)
- Loan statements / sanction letters → `Liability` (lender name, EMI, annual interest
  rate %; sanction details — original principal, EMI start date, original tenure — when
  present, else outstanding principal + remaining tenure as fallback)
- Confirm-and-correct review screen; nothing is ever auto-applied

**Out (v1.1+):** tax documents (ITR / Form 16 / 26AS), bank-statement expense mining,
PDF share-intent, batch import, cloud extraction.

## 3. Architecture

```
Sources tab ── "Import document (PDF)" button
  → ACTION_OPEN_DOCUMENT (application/pdf)
  → PdfTextExtractor        (android.graphics.pdf.PdfRenderer → bitmaps, first 10 pages max
                             → existing OCRService (ML Kit) per page → page texts)
  → InputSanitizer          (existing prompt-injection defense, applied to OCR text)
  → DocumentParser          (classify: SALARY_SLIP | LOAN_STATEMENT | UNKNOWN;
                             extract: Gemma when installed, regex fallback otherwise —
                             the exact SmsParser/EmailParser pattern)
  → ExtractDocumentUseCase  (orchestration; returns DocumentExtraction)
  → DocumentReviewScreen    (editable pre-filled fields; manual type picker on UNKNOWN)
  → Confirm → FireRepository.saveFireModel(model.copy(incomes/liabilities = +new))
  → projection recomputes everywhere via the existing Flow
```

No new dependencies. No DB migration (`FireModel` is a GSON blob; `IncomeStream` and
`Liability` already exist).

## 4. Components

### `ai/PdfTextExtractor` (new)
- `suspend fun extract(uri: Uri): List<String>` — one string per page.
- Opens the Uri via ContentResolver (copy to cache file — PdfRenderer needs a
  seekable ParcelFileDescriptor), renders each page at ~2x density for OCR quality,
  runs the existing `OCRService` per page. Caps at the first **10 pages**.
- Thin and Android-only; excluded from JVM unit tests, covered by the manual device
  checklist.

### `ai/DocumentParser` (new)
- `fun classify(text: String): DocType` — keyword heuristics (e.g. "salary", "net pay",
  "payslip" vs "EMI", "loan account", "outstanding", "sanction").
- `suspend fun parse(text: String, type: DocType): DocumentExtraction` — Gemma via a
  new `PromptTemplates` prompt when the model is installed; regex fallback otherwise.
  Skip guard: if no amount is found by either path, extraction fields stay null and the
  review screen shows the raw text (same honesty rule as the SMS/email skip guard).

### `domain/usecase/ExtractDocumentUseCase` (new)
- Orchestrates extractor → sanitizer → parser.
- Returns `DocumentExtraction(type, salary: SalaryFields?, loan: LoanFields?,
  textPreview: String)`.

### Extraction result hand-off
- `@Singleton ExtractionResultHolder` (StateFlow) injected via Hilt — Compose nav args
  can't carry rich objects; the holder is the simplest correct hand-off between the
  Sources screen (which runs the use case) and the review screen.

### `ui/screens/docimport/DocumentReviewScreen` + ViewModel (new)
- Editable pre-filled fields in the FIRE Settings form style (₹ prefixes, numeric
  keyboards, theme tokens only).
- `UNKNOWN` classification → manual type picker (Salary / Loan) over the raw text.
- Confirm → read-modify-write on `FireRepository` (single user-mediated writer; no
  concurrency guard needed — same posture as `FireModelViewModel.save()`).

### Sources screen (modify)
- "Import document (PDF)" button → file picker → progress state while extracting →
  navigate to review screen.

## 5. Field mappings

| Doc type | Extracted | Maps to |
|---|---|---|
| Salary slip | employer, net pay (monthly) | `IncomeStream(owner="Self", label=employer, monthlyNet=netPay, effectiveFrom=today)` |
| Loan statement | lender, EMI, annual rate %, **sanction details when present** (original/sanctioned principal, EMI start or first-disbursement date, original tenure months); otherwise outstanding principal + remaining tenure | See loan mapping rule below |

**Loan mapping rule (sanction-first):**
1. **Sanction details present** (sanctioned principal + EMI start date + original tenure —
   common on sanction letters and the first page of loan statements):
   `Liability(name=lender, principalAtEmiStart=sanctionedPrincipal, annualRatePct=rate,
   tenureMonths=originalTenure, emiStartDate=parsedStartDate)`. This is exact — the
   engine's declining-balance amortization then derives today's outstanding itself.
2. **Fallback (only outstanding + remaining tenure found):** `principalAtEmiStart =
   current outstanding`, `tenureMonths = remaining`, `emiStartDate = today`. Correct for
   a forward-looking projection; the review screen labels it "estimated from current
   balance" so the user knows which mode applied.

The parser extracts **both** field sets when available; the mapping prefers sanction
details. All fields are editable on the review screen either way.

## 6. Error handling

- Password-protected / corrupt PDF (`SecurityException` / `IOException` from
  PdfRenderer) → user-facing message, no crash.
- Empty OCR output → "Couldn't read this document" message.
- `UNKNOWN` classification → review screen still opens with raw text + manual type
  picker; user can salvage or cancel.
- Every extracted field is editable before Confirm; Cancel discards everything.

## 7. Testing

- **JVM unit tests** with realistic OCR-text fixtures (Indian salary slip and loan
  statement shaped text): classification, salary regex extraction, loan regex
  extraction, Gemma path with mocked `GemmaService` (the `ProcessSmsUseCaseTest`
  pattern), extraction→domain mapping.
- **Manual device checklist:** import a real salary slip PDF and a loan statement PDF
  from `C:\Users\wolfs\Downloads\Home_loan\`; verify fields, confirm, check the
  projection updates; verify password-protected PDF shows the error message.
- Test runs from WSL use the Windows-Gradle batch trick (documented in project memory).

## 8. Global constraints (carried from Spec 1/1.5)

- Package `com.expenseai` unchanged; no new dependencies; dark theme tokens only.
- Currency display: en-IN ₹ formatter, `maximumFractionDigits = 0`; amount inputs use
  numeric keyboards.
- OCR/LLM inputs pass through `InputSanitizer` before any prompt.
- Commit per task; push only after the full plan is green.
