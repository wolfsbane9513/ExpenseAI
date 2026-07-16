# Spec 2: On-Device Document Extraction Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Import a salary-slip or loan-statement PDF, extract the fields the FIRE projection needs on-device (PdfRenderer → ML Kit OCR → Gemma/regex), and merge them into the `FireModel` after user confirm-and-correct.

**Architecture:** A new `DocumentParser` (classification + regex fallback, Gemma-preferred per-field merge) mirrors the existing `SmsParser`/`EmailParser` pattern. `PdfTextExtractor` (Android `PdfRenderer` + existing `OCRService`) feeds it via `ExtractDocumentUseCase`. Results hand off through a Hilt-singleton `ExtractionResultHolder` to a new `DocumentReviewScreen`, which appends an `IncomeStream` or `Liability` to the GSON-persisted `FireModel`. No DB migration, no new dependencies, no network.

**Tech Stack:** Kotlin, Jetpack Compose (Material 3), Hilt, `android.graphics.pdf.PdfRenderer`, ML Kit OCR (existing `OCRService`), MediaPipe Gemma (existing `GemmaService`), JUnit4 + MockK.

## Global Constraints

- Package `com.expenseai` unchanged; **no new dependencies**; everything on-device (no network).
- Dark theme tokens only (`MaterialTheme.colorScheme.*`, no raw hex in screens).
- Currency display: `NumberFormat.getCurrencyInstance(Locale("en", "IN"))` with `maximumFractionDigits = 0`; amount inputs use `KeyboardOptions(keyboardType = KeyboardType.Number)`.
- All OCR/LLM input passes through `InputSanitizer` before prompt construction.
- Tests: `./gradlew testDebugUnitTest`. From WSL there is no JVM — write a `.bat` that sets `JAVA_HOME=C:\Program Files\Android\Android Studio\jbr` and runs `java -cp gradle\wrapper\gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain testDebugUnitTest --console=plain`, invoke via `cmd.exe /c` (no `gradlew.bat` exists in the repo).
- Commit after each task; push only after the whole plan is green and the user authorizes.

## Design decisions locked in

1. **Regex fallback is the floor, Gemma is per-field gravy.** `DocumentParser` computes regex extraction always; when Gemma is available its non-null fields win (`gemma.field ?: regex.field`). No Gemma → pure regex, same as SMS/email.
2. **Sanction-first loan mapping** (spec §5): if sanctioned principal + EMI start date + original tenure are all present, build the `Liability` from them (engine amortizes to today's outstanding itself); otherwise fall back to outstanding-as-principal with `emiStartDate = today`, and the review screen labels the fallback "Estimated from current balance".
3. **Sanitizer sizing:** existing `sanitizeOcrText` caps at 5,000 chars — too small for 10-page documents. Task 1 adds `sanitizeDocumentText` (identical filtering, 20,000-char cap). The Gemma prompt receives only the first 6,000 chars (small-model context); the regex path sees the full sanitized text.
4. **Hand-off via `ExtractionResultHolder`** (Hilt `@Singleton` StateFlow) because Compose nav args can't carry rich objects.
5. **Nothing auto-applies.** Confirm on the review screen is the only write; it appends to `FireModel.incomes` or `.liabilities` via read-modify-write (single user-mediated writer — same posture as `FireModelViewModel.save()`).

---

### Task 1: DocumentParser — classification + regex extraction (pure Kotlin, TDD)

**Files:**
- Create: `app/src/main/java/com/expenseai/ai/DocumentParser.kt`
- Modify: `app/src/main/java/com/expenseai/security/InputSanitizer.kt` (add `sanitizeDocumentText`)
- Test: `app/src/test/java/com/expenseai/DocumentParserTest.kt`

**Interfaces:**
- Consumes: nothing new (pure Kotlin + `java.time.LocalDate`).
- Produces: `enum class DocType { SALARY_SLIP, LOAN_STATEMENT, UNKNOWN }`; `data class SalaryFields(employer: String?, monthlyNet: Double?)`; `data class LoanFields(lender: String?, emi: Double?, annualRatePct: Double?, sanctionedPrincipal: Double?, emiStartDate: LocalDate?, originalTenureMonths: Int?, outstandingPrincipal: Double?, remainingTenureMonths: Int?)`; `data class DocumentExtraction(type: DocType, salary: SalaryFields? = null, loan: LoanFields? = null, textPreview: String = "")`; `class DocumentParser @Inject constructor()` with `fun classify(text: String): DocType`, `internal fun extractSalary(text: String): SalaryFields`, `internal fun extractLoan(text: String): LoanFields`; `InputSanitizer.sanitizeDocumentText(raw: String): String` (20,000-char cap). Task 2 adds the `parse(...)` orchestration and the GemmaService constructor param.

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/expenseai/DocumentParserTest.kt`:

```kotlin
package com.expenseai

import com.expenseai.ai.DocType
import com.expenseai.ai.DocumentParser
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class DocumentParserTest {

    private val parser = DocumentParser()

    private val salarySlipText = """
        TransmuteLabs Private Limited
        Payslip for the month of June 2026
        Employee Name: R Prakash
        Basic Pay 1,20,000.00
        HRA 48,000.00
        Gross Pay 1,95,000.00
        Deductions 45,000.00
        Net Pay 1,50,000.00
    """.trimIndent()

    private val loanSanctionText = """
        State Bank of India
        Home Loan Account Statement
        Loan Account No: 000123456789
        Sanctioned Amount: 85,00,000.00
        Rate of Interest: 8.50 %
        Tenure: 300 months
        First EMI Date: 05/09/2024
        EMI: 68,444.00
        Outstanding Principal: 82,10,550.00
    """.trimIndent()

    private val loanOutstandingOnlyText = """
        HDFC Bank Loan Statement
        EMI Amount 25,000.00
        Interest Rate 9.1%
        Outstanding Balance: 12,40,000.00
        Remaining Tenure: 62 months
    """.trimIndent()

    private val randomText = "Your OTP for login is 482913. Do not share it with anyone."

    @Test
    fun `classifies salary slip`() {
        assertEquals(DocType.SALARY_SLIP, parser.classify(salarySlipText))
    }

    @Test
    fun `classifies loan statement`() {
        assertEquals(DocType.LOAN_STATEMENT, parser.classify(loanSanctionText))
    }

    @Test
    fun `unrelated text classifies as unknown`() {
        assertEquals(DocType.UNKNOWN, parser.classify(randomText))
    }

    @Test
    fun `extracts net pay and employer from salary slip`() {
        val fields = parser.extractSalary(salarySlipText)
        assertEquals(150000.0, fields.monthlyNet!!, 0.01)
        assertEquals("TransmuteLabs Private Limited", fields.employer)
    }

    @Test
    fun `extracts sanction details from loan statement`() {
        val fields = parser.extractLoan(loanSanctionText)
        assertEquals(8500000.0, fields.sanctionedPrincipal!!, 0.01)
        assertEquals(8.5, fields.annualRatePct!!, 0.001)
        assertEquals(300, fields.originalTenureMonths)
        assertEquals(LocalDate.of(2024, 9, 5), fields.emiStartDate)
        assertEquals(68444.0, fields.emi!!, 0.01)
        assertEquals(8210550.0, fields.outstandingPrincipal!!, 0.01)
        assertEquals("State Bank of India", fields.lender)
    }

    @Test
    fun `extracts outstanding fallback fields when no sanction details`() {
        val fields = parser.extractLoan(loanOutstandingOnlyText)
        assertNull(fields.sanctionedPrincipal)
        assertNull(fields.emiStartDate)
        assertEquals(1240000.0, fields.outstandingPrincipal!!, 0.01)
        assertEquals(62, fields.remainingTenureMonths)
        assertEquals(25000.0, fields.emi!!, 0.01)
        assertEquals(9.1, fields.annualRatePct!!, 0.001)
    }

    @Test
    fun `tenure in years converts to months`() {
        val fields = parser.extractLoan("Loan Sanctioned Amount 50,00,000 Tenure: 25 years EMI 40,000")
        assertEquals(300, fields.originalTenureMonths)
    }

    @Test
    fun `sanitizeDocumentText keeps long documents`() {
        val long = "a".repeat(10_000)
        assertEquals(10_000, com.expenseai.security.InputSanitizer.sanitizeDocumentText(long).length)
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.DocumentParserTest` (via the WSL `.bat` mechanism in Global Constraints)
Expected: compilation FAILURE — `DocumentParser`, `DocType`, `sanitizeDocumentText` unresolved.

- [ ] **Step 3: Implement**

Add to `InputSanitizer.kt` (next to `sanitizeOcrText`, reusing its filtering; add `MAX_DOCUMENT_LENGTH` to the companion constants):

```kotlin
    // Sanitize multi-page document OCR text (longer cap than single-image OCR)
    fun sanitizeDocumentText(ocrText: String): String {
        return ocrText
            .replace(Regex("[<>\"';`]"), "")
            .replace(Regex("\\s{3,}"), " ")
            .take(MAX_DOCUMENT_LENGTH)
    }
```
```kotlin
    private const val MAX_DOCUMENT_LENGTH = 20_000
```
(Match the exact filtering regexes used by the existing `sanitizeOcrText` body — read it first and reuse its replace chain, changing only the cap.)

Create `app/src/main/java/com/expenseai/ai/DocumentParser.kt`:

```kotlin
package com.expenseai.ai

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

enum class DocType { SALARY_SLIP, LOAN_STATEMENT, UNKNOWN }

data class SalaryFields(
    val employer: String? = null,
    val monthlyNet: Double? = null
)

data class LoanFields(
    val lender: String? = null,
    val emi: Double? = null,
    val annualRatePct: Double? = null,
    val sanctionedPrincipal: Double? = null,
    val emiStartDate: LocalDate? = null,
    val originalTenureMonths: Int? = null,
    val outstandingPrincipal: Double? = null,
    val remainingTenureMonths: Int? = null
)

data class DocumentExtraction(
    val type: DocType,
    val salary: SalaryFields? = null,
    val loan: LoanFields? = null,
    val textPreview: String = ""
)

@Singleton
class DocumentParser @Inject constructor() {

    fun classify(text: String): DocType {
        val lower = text.lowercase()
        val salaryScore = SALARY_KEYWORDS.count { it in lower }
        val loanScore = LOAN_KEYWORDS.count { it in lower }
        return when {
            salaryScore >= 2 && salaryScore >= loanScore -> DocType.SALARY_SLIP
            loanScore >= 2 -> DocType.LOAN_STATEMENT
            else -> DocType.UNKNOWN
        }
    }

    internal fun extractSalary(text: String): SalaryFields = SalaryFields(
        employer = text.lineSequence()
            .map { it.trim() }
            .firstOrNull { it.length >= 4 && it.any(Char::isLetter) },
        monthlyNet = amountAfter(text, "net pay|net salary|net amount|take home")
    )

    internal fun extractLoan(text: String): LoanFields = LoanFields(
        lender = KNOWN_LENDERS.firstOrNull { text.contains(it, ignoreCase = true) }
            ?: text.lineSequence().map { it.trim() }
                .firstOrNull { it.length >= 4 && it.any(Char::isLetter) },
        emi = amountAfter(text, "emi(?: amount)?"),
        annualRatePct = Regex(
            "(?:rate of interest|interest rate|roi)\\D{0,20}?([0-9]+(?:\\.[0-9]+)?)\\s*%",
            RegexOption.IGNORE_CASE
        ).find(text)?.groupValues?.get(1)?.toDoubleOrNull(),
        sanctionedPrincipal = amountAfter(text, "sanction(?:ed)?\\s*(?:amount|limit)|loan amount"),
        emiStartDate = dateAfter(text, "first emi date|emi start(?: date)?|commencement date|date of commencement"),
        originalTenureMonths = tenureMonths(text, sanctionContext = true),
        outstandingPrincipal = amountAfter(text, "outstanding\\s*(?:principal|balance|amount)"),
        remainingTenureMonths = tenureAfter(text, "remaining tenure|balance tenure")
    )

    private fun amountAfter(text: String, labelPattern: String): Double? =
        Regex("(?:$labelPattern)\\s*:?\\s*(?:rs\\.?|inr|₹)?\\s*([0-9][0-9,]*(?:\\.[0-9]{1,2})?)", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull()

    private fun dateAfter(text: String, labelPattern: String): LocalDate? {
        val raw = Regex("(?:$labelPattern)\\s*:?\\s*([0-9]{1,2}[-/][0-9]{1,2}[-/][0-9]{2,4})", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.get(1) ?: return null
        val normalized = raw.replace('/', '-')
        val parts = normalized.split("-")
        val year = parts[2].let { if (it.length == 2) "20$it" else it }
        return runCatching {
            LocalDate.parse("${parts[0].padStart(2, '0')}-${parts[1].padStart(2, '0')}-$year",
                DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }.getOrNull()
    }

    private fun tenureAfter(text: String, labelPattern: String): Int? {
        val m = Regex("(?:$labelPattern)\\s*:?\\s*([0-9]{1,3})\\s*(months?|years?)", RegexOption.IGNORE_CASE)
            .find(text) ?: return null
        val n = m.groupValues[1].toIntOrNull() ?: return null
        return if (m.groupValues[2].startsWith("year", ignoreCase = true)) n * 12 else n
    }

    // Plain "Tenure: N months/years" (not "remaining tenure") -> original tenure
    private fun tenureMonths(text: String, sanctionContext: Boolean): Int? {
        if (!sanctionContext) return null
        val m = Regex("(?<!remaining )(?<!balance )tenure\\s*:?\\s*([0-9]{1,3})\\s*(months?|years?)", RegexOption.IGNORE_CASE)
            .find(text) ?: return null
        val n = m.groupValues[1].toIntOrNull() ?: return null
        return if (m.groupValues[2].startsWith("year", ignoreCase = true)) n * 12 else n
    }

    companion object {
        private val SALARY_KEYWORDS = listOf(
            "payslip", "pay slip", "salary", "net pay", "gross pay", "basic pay", "earnings", "take home"
        )
        private val LOAN_KEYWORDS = listOf(
            "emi", "loan account", "sanction", "outstanding", "rate of interest",
            "interest rate", "disbursement", "tenure"
        )
        private val KNOWN_LENDERS = listOf(
            "State Bank of India", "SBI", "HDFC", "ICICI", "Axis Bank", "Kotak",
            "LIC Housing", "PNB", "Bank of Baroda", "Canara Bank"
        )
    }
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.DocumentParserTest`
Expected: PASS (9 tests). If a regex misses a fixture, fix the regex — do not weaken the fixture.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/ai/DocumentParser.kt app/src/main/java/com/expenseai/security/InputSanitizer.kt app/src/test/java/com/expenseai/DocumentParserTest.kt
git commit -m "feat(docs-import): document classifier and regex field extraction"
```

---

### Task 2: Gemma path — prompt, ParsedDocument, per-field merge

**Files:**
- Modify: `app/src/main/java/com/expenseai/ai/PromptTemplates.kt` (add `documentParsingPrompt`)
- Modify: `app/src/main/java/com/expenseai/ai/GemmaService.kt` (add `ParsedDocument` + `parseDocument`)
- Modify: `app/src/main/java/com/expenseai/ai/DocumentParser.kt` (constructor gains `GemmaService`; add `parse`)
- Test: `app/src/test/java/com/expenseai/DocumentParserGemmaTest.kt`

**Interfaces:**
- Consumes: Task 1 types; `GemmaService` internals (`isInitialized` gate pattern, `runInference`, markdown-fence JSON cleanup as in `parseJsonResponse`).
- Produces: `data class ParsedDocument(docType: String = "", employer: String? = null, monthlyNet: Double? = null, lender: String? = null, emi: Double? = null, annualRatePct: Double? = null, sanctionedPrincipal: Double? = null, emiStartDate: String? = null, originalTenureMonths: Int? = null, outstandingPrincipal: Double? = null, remainingTenureMonths: Int? = null)`; `GemmaService.parseDocument(text: String): ParsedDocument`; `DocumentParser(gemmaService: GemmaService)` with `suspend fun parse(text: String, type: DocType): DocumentExtraction`.

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/expenseai/DocumentParserGemmaTest.kt`:

```kotlin
package com.expenseai

import com.expenseai.ai.DocType
import com.expenseai.ai.DocumentParser
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ParsedDocument
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DocumentParserGemmaTest {

    private lateinit var gemmaService: GemmaService
    private lateinit var parser: DocumentParser

    private val loanText = """
        HDFC Bank Loan Statement
        EMI Amount 25,000.00
        Interest Rate 9.1%
        Outstanding Balance: 12,40,000.00
        Remaining Tenure: 62 months
    """.trimIndent()

    @Before
    fun setup() {
        gemmaService = mockk(relaxed = true)
        parser = DocumentParser(gemmaService)
    }

    @Test
    fun `gemma fields win over regex when present`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument(
            docType = "loan", lender = "HDFC Home Loans", emi = 25500.0
        )
        val result = parser.parse(loanText, DocType.LOAN_STATEMENT)
        assertEquals("HDFC Home Loans", result.loan!!.lender)
        assertEquals(25500.0, result.loan!!.emi!!, 0.01)
        // regex fills what gemma left null
        assertEquals(9.1, result.loan!!.annualRatePct!!, 0.001)
        assertEquals(62, result.loan!!.remainingTenureMonths)
    }

    @Test
    fun `regex fallback used when gemma returns empty`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument()
        val result = parser.parse(loanText, DocType.LOAN_STATEMENT)
        assertEquals(1240000.0, result.loan!!.outstandingPrincipal!!, 0.01)
        assertEquals(25000.0, result.loan!!.emi!!, 0.01)
    }

    @Test
    fun `unknown type still returns text preview and no fields`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument()
        val result = parser.parse("random text with nothing useful", DocType.UNKNOWN)
        assertEquals(DocType.UNKNOWN, result.type)
        assertNull(result.salary)
        assertNull(result.loan)
        assertTrue(result.textPreview.isNotBlank())
    }

    @Test
    fun `gemma emiStartDate string parses to LocalDate`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument(
            docType = "loan", sanctionedPrincipal = 8500000.0,
            emiStartDate = "2024-09-05", originalTenureMonths = 300
        )
        val result = parser.parse(loanText, DocType.LOAN_STATEMENT)
        assertEquals(java.time.LocalDate.of(2024, 9, 5), result.loan!!.emiStartDate)
    }
}
```

Also update `DocumentParserTest.kt` from Task 1: construct the parser as `DocumentParser(mockk(relaxed = true))` (add `import io.mockk.mockk`) since the constructor gains a parameter.

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew testDebugUnitTest --tests "com.expenseai.DocumentParser*"`
Expected: compilation FAILURE — `ParsedDocument`, `parseDocument`, new constructor unresolved.

- [ ] **Step 3: Implement**

`PromptTemplates.kt` — append (same Gemma turn format as the existing prompts):

```kotlin
    fun documentParsingPrompt(documentText: String): String = buildString {
        append("<start_of_turn>system\n")
        append("You are a financial document parser for Indian salary slips and loan statements.\n")
        append("Always respond with valid JSON only, no other text.\n")
        append("<end_of_turn>\n")
        append("<start_of_turn>user\n")
        append("Parse this document. Use null for anything not present. Amounts are plain numbers (no commas).\n\n")
        append("Document text:\n")
        append(documentText)
        append("\n\n")
        append("Respond ONLY with JSON in this exact format:\n")
        append("""{"docType":"salary|loan|unknown","employer":null,"monthlyNet":null,"lender":null,"emi":null,"annualRatePct":null,"sanctionedPrincipal":null,"emiStartDate":"YYYY-MM-DD or null","originalTenureMonths":null,"outstandingPrincipal":null,"remainingTenureMonths":null}""")
        append("\n<end_of_turn>\n")
        append("<start_of_turn>model\n")
    }
```

`GemmaService.kt` — add next to `ParsedTransaction`:

```kotlin
data class ParsedDocument(
    val docType: String = "",
    val employer: String? = null,
    val monthlyNet: Double? = null,
    val lender: String? = null,
    val emi: Double? = null,
    val annualRatePct: Double? = null,
    val sanctionedPrincipal: Double? = null,
    val emiStartDate: String? = null,
    val originalTenureMonths: Int? = null,
    val outstandingPrincipal: Double? = null,
    val remainingTenureMonths: Int? = null
)
```

and add the method (note: uses `sanitizeDocumentText`, NOT `sanitizeTextInput` whose 500-char cap would truncate documents; prompt input capped at 6,000 chars for the small model's context):

```kotlin
    suspend fun parseDocument(documentText: String): ParsedDocument {
        val sanitized = InputSanitizer.sanitizeDocumentText(documentText).take(6000)
        if (!isInitialized) return ParsedDocument()

        return withContext(Dispatchers.IO) {
            try {
                val prompt = PromptTemplates.documentParsingPrompt(sanitized)
                val response = runInference(prompt)
                val cleaned = response.trim()
                    .removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                gson.fromJson(cleaned, ParsedDocument::class.java) ?: ParsedDocument()
            } catch (_: Exception) {
                ParsedDocument()
            }
        }
    }
```

`DocumentParser.kt` — constructor becomes `class DocumentParser @Inject constructor(private val gemmaService: GemmaService)`; add:

```kotlin
    suspend fun parse(text: String, type: DocType): DocumentExtraction {
        val preview = text.take(1200)
        if (type == DocType.UNKNOWN) return DocumentExtraction(DocType.UNKNOWN, textPreview = preview)

        val ai = gemmaService.parseDocument(text)
        return when (type) {
            DocType.SALARY_SLIP -> {
                val regex = extractSalary(text)
                DocumentExtraction(
                    type = type,
                    salary = SalaryFields(
                        employer = ai.employer ?: regex.employer,
                        monthlyNet = ai.monthlyNet ?: regex.monthlyNet
                    ),
                    textPreview = preview
                )
            }
            DocType.LOAN_STATEMENT -> {
                val regex = extractLoan(text)
                DocumentExtraction(
                    type = type,
                    loan = LoanFields(
                        lender = ai.lender ?: regex.lender,
                        emi = ai.emi ?: regex.emi,
                        annualRatePct = ai.annualRatePct ?: regex.annualRatePct,
                        sanctionedPrincipal = ai.sanctionedPrincipal ?: regex.sanctionedPrincipal,
                        emiStartDate = ai.emiStartDate
                            ?.let { runCatching { java.time.LocalDate.parse(it) }.getOrNull() }
                            ?: regex.emiStartDate,
                        originalTenureMonths = ai.originalTenureMonths ?: regex.originalTenureMonths,
                        outstandingPrincipal = ai.outstandingPrincipal ?: regex.outstandingPrincipal,
                        remainingTenureMonths = ai.remainingTenureMonths ?: regex.remainingTenureMonths
                    ),
                    textPreview = preview
                )
            }
            DocType.UNKNOWN -> DocumentExtraction(DocType.UNKNOWN, textPreview = preview)
        }
    }
```

- [ ] **Step 4: Run tests — new and Task 1 suites**

Run: `./gradlew testDebugUnitTest --tests "com.expenseai.DocumentParser*"`
Expected: PASS (13 tests). Then the full suite once: `./gradlew testDebugUnitTest` — expected 87 existing + 13 = 100 green.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/ai/ app/src/test/java/com/expenseai/
git commit -m "feat(docs-import): Gemma document parsing with per-field regex merge"
```

---

### Task 3: PdfTextExtractor + ExtractDocumentUseCase + ExtractionResultHolder

**Files:**
- Create: `app/src/main/java/com/expenseai/ai/PdfTextExtractor.kt`
- Create: `app/src/main/java/com/expenseai/domain/usecase/ExtractDocumentUseCase.kt`
- Create: `app/src/main/java/com/expenseai/ui/screens/docimport/ExtractionResultHolder.kt`
- Test: `app/src/test/java/com/expenseai/ExtractDocumentUseCaseTest.kt`

**Interfaces:**
- Consumes: `OCRService.extractText(bitmap: Bitmap): String` (existing); `DocumentParser.classify/parse` (Tasks 1–2); `InputSanitizer.sanitizeDocumentText` (Task 1).
- Produces: `PdfTextExtractor.extract(uri: Uri): List<String>` (suspend; throws `IOException`/`SecurityException` on unreadable PDFs); `class EmptyDocumentException : Exception`; `ExtractDocumentUseCase.execute(uri: Uri): DocumentExtraction` (suspend); `ExtractionResultHolder` with `val extraction: StateFlow<DocumentExtraction?>`, `fun set(value: DocumentExtraction)`, `fun clear()`.

- [ ] **Step 1: Write the failing test (use case only — the extractor is Android-only)**

Create `app/src/test/java/com/expenseai/ExtractDocumentUseCaseTest.kt`:

```kotlin
package com.expenseai

import android.net.Uri
import com.expenseai.ai.DocType
import com.expenseai.ai.DocumentExtraction
import com.expenseai.ai.DocumentParser
import com.expenseai.ai.PdfTextExtractor
import com.expenseai.domain.usecase.EmptyDocumentException
import com.expenseai.domain.usecase.ExtractDocumentUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExtractDocumentUseCaseTest {

    private lateinit var pdfTextExtractor: PdfTextExtractor
    private lateinit var documentParser: DocumentParser
    private lateinit var useCase: ExtractDocumentUseCase
    private val uri = mockk<Uri>()

    @Before
    fun setup() {
        pdfTextExtractor = mockk()
        documentParser = mockk()
        useCase = ExtractDocumentUseCase(pdfTextExtractor, documentParser)
    }

    @Test
    fun `joins pages, classifies and parses`() = runTest {
        coEvery { pdfTextExtractor.extract(uri) } returns listOf("Payslip page 1", "Net Pay 1,50,000")
        every { documentParser.classify(any()) } returns DocType.SALARY_SLIP
        coEvery { documentParser.parse(any(), DocType.SALARY_SLIP) } returns
            DocumentExtraction(DocType.SALARY_SLIP, textPreview = "x")

        val result = useCase.execute(uri)
        assertEquals(DocType.SALARY_SLIP, result.type)
    }

    @Test(expected = EmptyDocumentException::class)
    fun `blank ocr output throws EmptyDocumentException`() = runTest {
        coEvery { pdfTextExtractor.extract(uri) } returns listOf("", "   ")
        useCase.execute(uri)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.ExtractDocumentUseCaseTest`
Expected: compilation FAILURE — classes unresolved.

- [ ] **Step 3: Implement all three files**

`app/src/main/java/com/expenseai/ai/PdfTextExtractor.kt`:

```kotlin
package com.expenseai.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Renders PDF pages to bitmaps (built-in PdfRenderer) and OCRs each page. */
@Singleton
class PdfTextExtractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ocrService: OCRService
) {
    suspend fun extract(uri: Uri): List<String> = withContext(Dispatchers.IO) {
        val cacheFile = File(context.cacheDir, "import_doc.pdf")
        context.contentResolver.openInputStream(uri)?.use { input ->
            cacheFile.outputStream().use { output -> input.copyTo(output) }
        } ?: throw IOException("Cannot open document")

        try {
            ParcelFileDescriptor.open(cacheFile, ParcelFileDescriptor.MODE_READ_ONLY).use { pfd ->
                PdfRenderer(pfd).use { renderer ->
                    val pageCount = minOf(renderer.pageCount, MAX_PAGES)
                    (0 until pageCount).map { index ->
                        renderer.openPage(index).use { page ->
                            val bitmap = Bitmap.createBitmap(
                                page.width * RENDER_SCALE,
                                page.height * RENDER_SCALE,
                                Bitmap.Config.ARGB_8888
                            )
                            bitmap.eraseColor(Color.WHITE)
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            val text = ocrService.extractText(bitmap)
                            bitmap.recycle()
                            text
                        }
                    }
                }
            }
        } finally {
            cacheFile.delete()
        }
    }

    companion object {
        const val MAX_PAGES = 10
        private const val RENDER_SCALE = 2
    }
}
```

`app/src/main/java/com/expenseai/domain/usecase/ExtractDocumentUseCase.kt`:

```kotlin
package com.expenseai.domain.usecase

import android.net.Uri
import com.expenseai.ai.DocumentExtraction
import com.expenseai.ai.DocumentParser
import com.expenseai.ai.PdfTextExtractor
import com.expenseai.security.InputSanitizer
import javax.inject.Inject

class EmptyDocumentException : Exception("No readable text found in document")

class ExtractDocumentUseCase @Inject constructor(
    private val pdfTextExtractor: PdfTextExtractor,
    private val documentParser: DocumentParser
) {
    /** Extracts, sanitizes, classifies and parses a PDF. Throws on unreadable/empty docs. */
    suspend fun execute(uri: Uri): DocumentExtraction {
        val pages = pdfTextExtractor.extract(uri)
        val fullText = InputSanitizer.sanitizeDocumentText(pages.joinToString("\n\n"))
        if (fullText.isBlank()) throw EmptyDocumentException()
        val type = documentParser.classify(fullText)
        return documentParser.parse(fullText, type)
    }
}
```

`app/src/main/java/com/expenseai/ui/screens/docimport/ExtractionResultHolder.kt`:

```kotlin
package com.expenseai.ui.screens.docimport

import com.expenseai.ai.DocumentExtraction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Hands the extraction result from Sources to the review screen (nav args can't carry it). */
@Singleton
class ExtractionResultHolder @Inject constructor() {
    private val _extraction = MutableStateFlow<DocumentExtraction?>(null)
    val extraction: StateFlow<DocumentExtraction?> = _extraction.asStateFlow()

    fun set(value: DocumentExtraction) { _extraction.value = value }
    fun clear() { _extraction.value = null }
}
```

- [ ] **Step 4: Run tests**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.ExtractDocumentUseCaseTest`
Expected: PASS (2 tests). Note: mocking `android.net.Uri` works because it's only passed through, never dereferenced, and mockk creates the stub.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/ai/PdfTextExtractor.kt app/src/main/java/com/expenseai/domain/usecase/ExtractDocumentUseCase.kt app/src/main/java/com/expenseai/ui/screens/docimport/ app/src/test/java/com/expenseai/ExtractDocumentUseCaseTest.kt
git commit -m "feat(docs-import): PDF page OCR extractor and extraction use case"
```

---

### Task 4: Domain mappers (sanction-first) — pure functions, TDD

**Files:**
- Create: `app/src/main/java/com/expenseai/domain/usecase/DocumentMappers.kt`
- Test: `app/src/test/java/com/expenseai/DocumentMappersTest.kt`

**Interfaces:**
- Consumes: `SalaryFields`, `LoanFields` (Task 1); `IncomeStream`, `Liability` (`com.expenseai.domain.fire`).
- Produces: `fun SalaryFields.toIncomeStream(today: LocalDate = LocalDate.now()): IncomeStream`; `fun LoanFields.toLiability(today: LocalDate = LocalDate.now()): Liability`; `val LoanFields.usesSanctionDetails: Boolean` (true when sanctioned principal + start date + original tenure are all present — the review screen shows the "Estimated from current balance" label when false).

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/expenseai/DocumentMappersTest.kt`:

```kotlin
package com.expenseai

import com.expenseai.ai.LoanFields
import com.expenseai.ai.SalaryFields
import com.expenseai.domain.usecase.toIncomeStream
import com.expenseai.domain.usecase.toLiability
import com.expenseai.domain.usecase.usesSanctionDetails
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class DocumentMappersTest {

    private val today = LocalDate.of(2026, 7, 16)

    @Test
    fun `salary maps to income stream`() {
        val stream = SalaryFields(employer = "TransmuteLabs", monthlyNet = 150000.0)
            .toIncomeStream(today)
        assertEquals("TransmuteLabs", stream.label)
        assertEquals(150000.0, stream.monthlyNet, 0.01)
        assertEquals(today, stream.effectiveFrom)
        assertEquals("Self", stream.owner)
    }

    @Test
    fun `loan with sanction details maps exactly`() {
        val fields = LoanFields(
            lender = "SBI", emi = 68444.0, annualRatePct = 8.5,
            sanctionedPrincipal = 8500000.0,
            emiStartDate = LocalDate.of(2024, 9, 5),
            originalTenureMonths = 300,
            outstandingPrincipal = 8210550.0, remainingTenureMonths = 278
        )
        assertTrue(fields.usesSanctionDetails)
        val liability = fields.toLiability(today)
        assertEquals(8500000.0, liability.principalAtEmiStart, 0.01)
        assertEquals(300, liability.tenureMonths)
        assertEquals(LocalDate.of(2024, 9, 5), liability.emiStartDate)
        assertEquals(8.5, liability.annualRatePct, 0.001)
    }

    @Test
    fun `loan without sanction details falls back to outstanding from today`() {
        val fields = LoanFields(
            lender = "HDFC", emi = 25000.0, annualRatePct = 9.1,
            outstandingPrincipal = 1240000.0, remainingTenureMonths = 62
        )
        assertFalse(fields.usesSanctionDetails)
        val liability = fields.toLiability(today)
        assertEquals(1240000.0, liability.principalAtEmiStart, 0.01)
        assertEquals(62, liability.tenureMonths)
        assertEquals(today, liability.emiStartDate)
    }

    @Test
    fun `missing lender defaults to Loan`() {
        val liability = LoanFields(outstandingPrincipal = 100000.0, remainingTenureMonths = 12)
            .toLiability(today)
        assertEquals("Loan", liability.name)
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.DocumentMappersTest`
Expected: compilation FAILURE.

- [ ] **Step 3: Implement**

`app/src/main/java/com/expenseai/domain/usecase/DocumentMappers.kt`:

```kotlin
package com.expenseai.domain.usecase

import com.expenseai.ai.LoanFields
import com.expenseai.ai.SalaryFields
import com.expenseai.domain.fire.IncomeStream
import com.expenseai.domain.fire.Liability
import java.time.LocalDate

/** True when the exact sanction-time details were extracted (spec §5, mapping rule 1). */
val LoanFields.usesSanctionDetails: Boolean
    get() = sanctionedPrincipal != null && emiStartDate != null && originalTenureMonths != null

fun SalaryFields.toIncomeStream(today: LocalDate = LocalDate.now()): IncomeStream = IncomeStream(
    owner = "Self",
    label = employer?.takeIf { it.isNotBlank() } ?: "Salary",
    monthlyNet = monthlyNet ?: 0.0,
    effectiveFrom = today
)

fun LoanFields.toLiability(today: LocalDate = LocalDate.now()): Liability =
    if (usesSanctionDetails) {
        Liability(
            name = lender?.takeIf { it.isNotBlank() } ?: "Loan",
            principalAtEmiStart = sanctionedPrincipal!!,
            annualRatePct = annualRatePct ?: 0.0,
            tenureMonths = originalTenureMonths!!,
            emiStartDate = emiStartDate!!
        )
    } else {
        Liability(
            name = lender?.takeIf { it.isNotBlank() } ?: "Loan",
            principalAtEmiStart = outstandingPrincipal ?: 0.0,
            annualRatePct = annualRatePct ?: 0.0,
            tenureMonths = remainingTenureMonths ?: 0,
            emiStartDate = today
        )
    }
```

- [ ] **Step 4: Run tests**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.DocumentMappersTest`
Expected: PASS (4 tests).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/usecase/DocumentMappers.kt app/src/test/java/com/expenseai/DocumentMappersTest.kt
git commit -m "feat(docs-import): sanction-first mapping to IncomeStream/Liability"
```

---

### Task 5: DocumentReviewScreen + ViewModel + navigation

**Files:**
- Create: `app/src/main/java/com/expenseai/ui/screens/docimport/DocumentReviewViewModel.kt`
- Create: `app/src/main/java/com/expenseai/ui/screens/docimport/DocumentReviewScreen.kt`
- Modify: `app/src/main/java/com/expenseai/ui/navigation/NavGraph.kt` (route `"doc_review"`, Sources wiring)

**Interfaces:**
- Consumes: `ExtractionResultHolder` (Task 3), mappers (Task 4), `FireRepository.getFireModel()/saveFireModel()`, `DocType`/`DocumentExtraction` (Task 1).
- Produces: route `"doc_review"`; `DocumentReviewScreen(onDone: () -> Unit)`; `SourcesScreen` gains `onReviewExtraction: () -> Unit = {}` (wired in Task 6).

- [ ] **Step 1: ViewModel**

`app/src/main/java/com/expenseai/ui/screens/docimport/DocumentReviewViewModel.kt`:

```kotlin
package com.expenseai.ui.screens.docimport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.ai.DocType
import com.expenseai.ai.LoanFields
import com.expenseai.ai.SalaryFields
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.usecase.toIncomeStream
import com.expenseai.domain.usecase.toLiability
import com.expenseai.domain.usecase.usesSanctionDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DocReviewUiState(
    val type: DocType = DocType.UNKNOWN,
    val textPreview: String = "",
    // salary fields (editable text)
    val employer: String = "",
    val monthlyNet: String = "",
    // loan fields (editable text)
    val lender: String = "",
    val emi: String = "",
    val annualRatePct: String = "",
    val principal: String = "",
    val tenureMonths: String = "",
    val emiStartDate: LocalDate? = null,
    val estimatedFromCurrentBalance: Boolean = false,
    val isSaved: Boolean = false,
    val hasExtraction: Boolean = false
)

@HiltViewModel
class DocumentReviewViewModel @Inject constructor(
    private val holder: ExtractionResultHolder,
    private val fireRepository: FireRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DocReviewUiState())
    val uiState: StateFlow<DocReviewUiState> = _uiState.asStateFlow()

    init {
        holder.extraction.value?.let { extraction ->
            val loan = extraction.loan
            val sanction = loan?.usesSanctionDetails == true
            _uiState.value = DocReviewUiState(
                type = extraction.type,
                textPreview = extraction.textPreview,
                employer = extraction.salary?.employer.orEmpty(),
                monthlyNet = extraction.salary?.monthlyNet?.toLong()?.toString().orEmpty(),
                lender = loan?.lender.orEmpty(),
                emi = loan?.emi?.toLong()?.toString().orEmpty(),
                annualRatePct = loan?.annualRatePct?.toString().orEmpty(),
                principal = (if (sanction) loan?.sanctionedPrincipal else loan?.outstandingPrincipal)
                    ?.toLong()?.toString().orEmpty(),
                tenureMonths = (if (sanction) loan?.originalTenureMonths else loan?.remainingTenureMonths)
                    ?.toString().orEmpty(),
                emiStartDate = if (sanction) loan?.emiStartDate else null,
                estimatedFromCurrentBalance = extraction.type == DocType.LOAN_STATEMENT && !sanction,
                hasExtraction = true
            )
        }
    }

    fun setType(type: DocType) = _uiState.update { it.copy(type = type) }
    fun updateEmployer(v: String) = _uiState.update { it.copy(employer = v) }
    fun updateMonthlyNet(v: String) = _uiState.update { it.copy(monthlyNet = v.filter(Char::isDigit)) }
    fun updateLender(v: String) = _uiState.update { it.copy(lender = v) }
    fun updateEmi(v: String) = _uiState.update { it.copy(emi = v.filter(Char::isDigit)) }
    fun updateRate(v: String) = _uiState.update { it.copy(annualRatePct = v.filter { c -> c.isDigit() || c == '.' }) }
    fun updatePrincipal(v: String) = _uiState.update { it.copy(principal = v.filter(Char::isDigit)) }
    fun updateTenure(v: String) = _uiState.update { it.copy(tenureMonths = v.filter(Char::isDigit)) }

    fun confirm() {
        val s = _uiState.value
        viewModelScope.launch {
            val model = fireRepository.getFireModel().first()
            val updated = when (s.type) {
                DocType.SALARY_SLIP -> model.copy(
                    incomes = model.incomes + SalaryFields(
                        employer = s.employer,
                        monthlyNet = s.monthlyNet.toDoubleOrNull()
                    ).toIncomeStream()
                )
                DocType.LOAN_STATEMENT -> {
                    val sanction = s.emiStartDate != null
                    model.copy(
                        liabilities = model.liabilities + LoanFields(
                            lender = s.lender,
                            emi = s.emi.toDoubleOrNull(),
                            annualRatePct = s.annualRatePct.toDoubleOrNull(),
                            sanctionedPrincipal = if (sanction) s.principal.toDoubleOrNull() else null,
                            emiStartDate = s.emiStartDate,
                            originalTenureMonths = if (sanction) s.tenureMonths.toIntOrNull() else null,
                            outstandingPrincipal = if (!sanction) s.principal.toDoubleOrNull() else null,
                            remainingTenureMonths = if (!sanction) s.tenureMonths.toIntOrNull() else null
                        ).toLiability()
                    )
                }
                DocType.UNKNOWN -> model
            }
            if (updated !== model) {
                fireRepository.saveFireModel(updated)
            }
            holder.clear()
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun cancel() = holder.clear()

    val canConfirm: Boolean
        get() = when (_uiState.value.type) {
            DocType.SALARY_SLIP -> _uiState.value.monthlyNet.toDoubleOrNull()?.let { it > 0 } == true
            DocType.LOAN_STATEMENT -> _uiState.value.principal.toDoubleOrNull()?.let { it > 0 } == true &&
                (_uiState.value.tenureMonths.toIntOrNull() ?: 0) > 0
            DocType.UNKNOWN -> false
        }
}
```

- [ ] **Step 2: Screen**

`app/src/main/java/com/expenseai/ui/screens/docimport/DocumentReviewScreen.kt`:

```kotlin
package com.expenseai.ui.screens.docimport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.ai.DocType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentReviewScreen(
    onDone: () -> Unit,
    viewModel: DocumentReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onDone()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Extraction", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.cancel(); onDone() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancel")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!uiState.hasExtraction) {
                Text(
                    "No extraction to review.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                return@Column
            }

            if (uiState.type == DocType.UNKNOWN) {
                Text(
                    "Couldn't recognise this document. Pick a type to fill the fields manually:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.type == DocType.SALARY_SLIP,
                    onClick = { viewModel.setType(DocType.SALARY_SLIP) },
                    label = { Text("Salary slip") }
                )
                FilterChip(
                    selected = uiState.type == DocType.LOAN_STATEMENT,
                    onClick = { viewModel.setType(DocType.LOAN_STATEMENT) },
                    label = { Text("Loan") }
                )
            }

            when (uiState.type) {
                DocType.SALARY_SLIP -> {
                    OutlinedTextField(
                        value = uiState.employer,
                        onValueChange = viewModel::updateEmployer,
                        label = { Text("Employer / income label") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.monthlyNet,
                        onValueChange = viewModel::updateMonthlyNet,
                        label = { Text("Monthly net pay") },
                        prefix = { Text("₹ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                DocType.LOAN_STATEMENT -> {
                    if (uiState.estimatedFromCurrentBalance) {
                        Text(
                            "Estimated from current balance — sanction details weren't found, " +
                                "so the loan is modelled from today's outstanding amount.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    OutlinedTextField(
                        value = uiState.lender,
                        onValueChange = viewModel::updateLender,
                        label = { Text("Lender") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.principal,
                        onValueChange = viewModel::updatePrincipal,
                        label = {
                            Text(if (uiState.emiStartDate != null) "Sanctioned principal" else "Outstanding principal")
                        },
                        prefix = { Text("₹ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.annualRatePct,
                        onValueChange = viewModel::updateRate,
                        label = { Text("Interest rate (annual)") },
                        suffix = { Text("%") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.tenureMonths,
                        onValueChange = viewModel::updateTenure,
                        label = {
                            Text(if (uiState.emiStartDate != null) "Original tenure (months)" else "Remaining tenure (months)")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    uiState.emiStartDate?.let {
                        Text(
                            "First EMI date: $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                DocType.UNKNOWN -> Unit
            }

            HorizontalDivider()
            Text(
                "Extracted text (first part)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(uiState.textPreview, style = MaterialTheme.typography.bodySmall)

            Button(
                onClick = viewModel::confirm,
                enabled = viewModel.canConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    when (uiState.type) {
                        DocType.SALARY_SLIP -> "Add income to FIRE model"
                        DocType.LOAN_STATEMENT -> "Add loan to FIRE model"
                        DocType.UNKNOWN -> "Pick a type first"
                    }
                )
            }
        }
    }
}
```

- [ ] **Step 3: NavGraph wiring**

In `NavGraph.kt`: add to the `Screen` sealed class

```kotlin
    data object DocReview : Screen("doc_review", "Review", Icons.Default.Description)
```

(add import `androidx.compose.material.icons.filled.Description`; NOT added to `bottomNavItems`). Add the route inside `NavHost` after the `ShouldIBuy` route:

```kotlin
            composable(Screen.DocReview.route) {
                DocumentReviewScreen(onDone = { navController.popBackStack() })
            }
```

Change the Sources route (non-preview branch) to:

```kotlin
            composable(Screen.Sources.route) {
                if (!isShowingPreview) {
                    SourcesScreen(
                        onReviewExtraction = { navController.navigate(Screen.DocReview.route) }
                    )
                } else {
                    Text("Sources Screen Preview")
                }
            }
```

Add import `com.expenseai.ui.screens.docimport.DocumentReviewScreen`. `SourcesScreen` gets the `onReviewExtraction: () -> Unit = {}` parameter in Task 6 — to keep this task compiling on its own, Task 5 adds the parameter to `SourcesScreen`'s signature with a default value and nothing else:

```kotlin
fun SourcesScreen(
    onReviewExtraction: () -> Unit = {},
    viewModel: SourcesViewModel = hiltViewModel()
) {
```

- [ ] **Step 4: Compile + full suite**

Run: `./gradlew testDebugUnitTest`
Expected: BUILD SUCCESSFUL (no new unit tests in this task — mapping logic was tested in Task 4; this is Compose/nav plumbing).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/ui/
git commit -m "feat(docs-import): document review screen with confirm-and-correct"
```

---

### Task 6: Sources screen — import button, progress, errors

**Files:**
- Modify: `app/src/main/java/com/expenseai/ui/screens/sources/SourcesViewModel.kt`
- Modify: `app/src/main/java/com/expenseai/ui/screens/sources/SourcesScreen.kt`

**Interfaces:**
- Consumes: `ExtractDocumentUseCase.execute(uri)` + `EmptyDocumentException` (Task 3), `ExtractionResultHolder` (Task 3), `SourcesScreen(onReviewExtraction)` (Task 5).
- Produces: `SourcesViewModel.importDocument(uri: Uri)`; `sealed interface ImportState { Idle, Loading, Ready, Error(message) }` exposed as `StateFlow<ImportState>` plus `fun resetImportState()`.

- [ ] **Step 1: ViewModel additions**

In `SourcesViewModel.kt` add constructor params `private val extractDocumentUseCase: ExtractDocumentUseCase, private val extractionResultHolder: ExtractionResultHolder` and:

```kotlin
    sealed interface ImportState {
        data object Idle : ImportState
        data object Loading : ImportState
        data object Ready : ImportState
        data class Error(val message: String) : ImportState
    }

    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState: StateFlow<ImportState> = _importState.asStateFlow()

    fun importDocument(uri: Uri) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading
            _importState.value = try {
                extractionResultHolder.set(extractDocumentUseCase.execute(uri))
                ImportState.Ready
            } catch (e: EmptyDocumentException) {
                ImportState.Error("Couldn't read this document — no text found.")
            } catch (e: SecurityException) {
                ImportState.Error("This PDF is password-protected.")
            } catch (e: Exception) {
                ImportState.Error("Couldn't open this document.")
            }
        }
    }

    fun resetImportState() { _importState.value = ImportState.Idle }
```

(Imports: `android.net.Uri`, `com.expenseai.domain.usecase.ExtractDocumentUseCase`, `com.expenseai.domain.usecase.EmptyDocumentException`, `com.expenseai.ui.screens.docimport.ExtractionResultHolder`, plus the flow/coroutine imports already present.)

- [ ] **Step 2: Screen additions**

In `SourcesScreen.kt` (inside the existing `Column`, after the pending-pulses button block), add:

```kotlin
            val importState by viewModel.importState.collectAsStateWithLifecycle()
            val documentPicker = rememberLauncherForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { uri -> uri?.let(viewModel::importDocument) }

            LaunchedEffect(importState) {
                if (importState is SourcesViewModel.ImportState.Ready) {
                    viewModel.resetImportState()
                    onReviewExtraction()
                }
            }

            Button(
                onClick = { documentPicker.launch(arrayOf("application/pdf")) },
                enabled = importState !is SourcesViewModel.ImportState.Loading,
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
            ) {
                if (importState is SourcesViewModel.ImportState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("READING DOCUMENT…", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("IMPORT DOCUMENT (PDF)", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
            }

            (importState as? SourcesViewModel.ImportState.Error)?.let { error ->
                Text(
                    text = error.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
```

Add imports: `androidx.activity.compose.rememberLauncherForActivityResult`, `androidx.activity.result.contract.ActivityResultContracts`, `androidx.compose.material.icons.filled.UploadFile`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.runtime.LaunchedEffect`, plus any of `Icon`/`Spacer`/`size` not already imported (check the file's existing imports first).

- [ ] **Step 3: Compile + full suite**

Run: `./gradlew testDebugUnitTest`
Expected: BUILD SUCCESSFUL, all tests green (~106: 87 pre-existing + 9 + 4 + 2 + 4 new).

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/expenseai/ui/screens/sources/
git commit -m "feat(docs-import): PDF import button with progress and error states"
```

---

### Task 7: Final verification

- [ ] **Step 1: Full suite**

Run: `./gradlew testDebugUnitTest`
Expected: BUILD SUCCESSFUL; ~106 tests, 0 failures (count from JUnit XML: `grep -c "<testcase" app/build/test-results/testDebugUnitTest/*.xml`).

- [ ] **Step 2: Manual device checklist** (requires device — cannot be automated here)

1. Sources tab → Import Document → pick a salary-slip PDF → fields pre-filled → Confirm → FIRE Settings/Projections reflect the new income.
2. Import a loan statement with sanction details → verify "Sanctioned principal"/"Original tenure" labels and the parsed first-EMI date; Confirm → projection updates (EMI reduces investable).
3. Import a loan statement without sanction details → "Estimated from current balance" note shows; Confirm works.
4. Import a random PDF → UNKNOWN flow: type picker + raw text; Cancel discards.
5. Import a password-protected PDF → error message, no crash.
6. With Gemma installed, repeat 1–2 and compare field quality vs. regex-only.

- [ ] **Step 3: Push (only after user confirmation or per session authorization)**

```bash
git push origin fire-os
```

---

## Self-review notes

- **Spec coverage:** §3 pipeline → Tasks 3+5+6; §4 components → Tasks 1–5 (one per component); §5 sanction-first mapping → Task 4 (+ review-screen labels in Task 5); §6 error handling → Task 6 (`EmptyDocumentException`/`SecurityException` branches) + Task 5 UNKNOWN picker; §7 tests → Tasks 1–4 fixtures + Task 7 manual checklist; sanitizer sizing (design decision 3) → Task 1.
- **Type consistency:** `SalaryFields`/`LoanFields`/`DocumentExtraction`/`DocType` (Task 1) consumed by Tasks 2–5 with matching shapes; `ParsedDocument` (Task 2) only inside `DocumentParser`; `ExtractionResultHolder` API (Task 3) matches Task 5/6 usage; `ImportState` names match between Task 6 VM and screen.
- **Known simplifications:** employer/lender first-line heuristic is crude — acceptable because every field is editable pre-confirm; `DocumentReviewViewModel.confirm()` is a read-modify-write without a mutex (single user-mediated writer, same posture as `FireModelViewModel.save()`); no VM unit test for the review screen (mapping logic extracted to Task 4's pure functions precisely so the VM stays thin).
- **Risk noted for implementers:** `ExtractDocumentUseCaseTest` mocks `android.net.Uri` — safe because the use case never dereferences it; if MockK complains, add `mockk<Uri>(relaxed = true)`.
