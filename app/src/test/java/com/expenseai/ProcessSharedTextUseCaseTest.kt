package com.expenseai

import com.expenseai.ai.EmailParser
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ParsedReceipt
import com.expenseai.ai.SmsParser
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.domain.usecase.ProcessSharedTextUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProcessSharedTextUseCaseTest {

    private lateinit var emailParser: EmailParser
    private lateinit var smsParser: SmsParser
    private lateinit var gemmaService: GemmaService
    private lateinit var pendingDao: PendingExpenseDao
    private lateinit var useCase: ProcessSharedTextUseCase

    @Before
    fun setup() {
        emailParser = mockk()
        smsParser = mockk()
        gemmaService = mockk(relaxed = true)
        pendingDao = mockk(relaxed = true)
        useCase = ProcessSharedTextUseCase(emailParser, smsParser, gemmaService, pendingDao)
        coEvery { gemmaService.categorizeExpense(any()) } answers { firstArg<String>().lowercase() }
    }

    @Test
    fun `email candidate is staged when subject indicates a receipt`() = runTest {
        every {
            emailParser.parse(any(), any())
        } returns ParsedReceipt(vendor = "Amazon", amount = 1299.0, date = "2026-04-09", category = "shopping")
        every { smsParser.parse(any()) } returns null
        coEvery { pendingDao.countByDedupKey(any()) } returns 0
        coEvery { pendingDao.insert(any()) } returns 1L

        val staged = useCase.execute(
            body = "Order Total Rs 1299.00",
            subject = "Your Amazon receipt"
        )

        assertTrue(staged)
        coVerify(exactly = 1) { pendingDao.insert(any()) }
    }

    @Test
    fun `sms markers prefer sms candidate when both parsers match`() = runTest {
        every { emailParser.parse(any(), any()) } returns ParsedReceipt("Mailbox Cafe", 250.0, "2026-04-09", "food")
        every { smsParser.parse(any()) } returns ParsedReceipt("Blue Tokai", 250.0, "2026-04-09", "food")
        coEvery { pendingDao.countByDedupKey(any()) } returns 0
        coEvery { pendingDao.insert(any()) } returns 1L

        val staged = useCase.execute(
            body = "Rs 250 debited at BLUETOKAI via UPI ref 123456789",
            subject = ""
        )

        assertTrue(staged)
        coVerify(exactly = 1) { pendingDao.insert(match { it.source == "sms" && it.vendor == "Blue Tokai" }) }
    }

    @Test
    fun `duplicate dedup key is not staged`() = runTest {
        every { emailParser.parse(any(), any()) } returns null
        every { smsParser.parse(any()) } returns ParsedReceipt("Blue Tokai", 250.0, "2026-04-09", "food")
        coEvery { pendingDao.countByDedupKey(any()) } returns 1

        val staged = useCase.execute(
            body = "Rs 250 debited at BLUETOKAI via UPI ref 123456789",
            subject = ""
        )

        assertFalse(staged)
        coVerify(exactly = 0) { pendingDao.insert(any()) }
    }

    @Test
    fun `unparseable shared text returns false`() = runTest {
        every { emailParser.parse(any(), any()) } returns null
        every { smsParser.parse(any()) } returns null

        val staged = useCase.execute(
            body = "hello there",
            subject = "just checking in"
        )

        assertFalse(staged)
        coVerify(exactly = 0) { pendingDao.insert(any()) }
    }
}
