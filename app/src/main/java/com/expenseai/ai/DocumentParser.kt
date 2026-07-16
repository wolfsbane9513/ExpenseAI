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
class DocumentParser @Inject constructor(private val gemmaService: GemmaService) {

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
