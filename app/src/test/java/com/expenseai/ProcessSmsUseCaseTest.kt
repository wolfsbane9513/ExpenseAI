package com.expenseai

import com.expenseai.ai.GemmaService
import com.expenseai.ai.ParsedReceipt
import com.expenseai.ai.RawSms
import com.expenseai.ai.SmsParser
import com.expenseai.ai.SmsReader
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.domain.usecase.ProcessSmsUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProcessSmsUseCaseTest {

    private lateinit var smsReader: SmsReader
    private lateinit var smsParser: SmsParser
    private lateinit var gemmaService: GemmaService
    private lateinit var pendingDao: PendingExpenseDao
    private lateinit var useCase: ProcessSmsUseCase

    @Before fun setup() {
        smsReader = mockk()
        smsParser = mockk()
        gemmaService = mockk(relaxed = true)
        pendingDao = mockk(relaxed = true)
        useCase = ProcessSmsUseCase(smsReader, smsParser, gemmaService, pendingDao)
    }

    @Test fun `staging new transaction SMS inserts into pending dao`() = runTest {
        val raw = RawSms("HDFC: Rs.500 debited on 06-04-2026. Ref 999", System.currentTimeMillis(), "HDFCBK")
        every { smsReader.readInbox() } returns listOf(raw)
        every { smsParser.parse(any()) } returns ParsedReceipt("Vendor", 500.0, "2026-04-06", "other")
        coEvery { pendingDao.countByDedupKey(any()) } returns 0
        coEvery { pendingDao.insert(any()) } returns 1L

        val count = useCase.execute()

        assertEquals(1, count)
        coVerify(exactly = 1) { pendingDao.insert(any()) }
    }

    @Test fun `duplicate dedup key is not inserted again`() = runTest {
        val raw = RawSms("HDFC: Rs.500 debited. Ref 999", System.currentTimeMillis(), "HDFCBK")
        every { smsReader.readInbox() } returns listOf(raw)
        every { smsParser.parse(any()) } returns ParsedReceipt("Vendor", 500.0, "2026-04-06", "other")
        coEvery { pendingDao.countByDedupKey(any()) } returns 1 // already staged

        val count = useCase.execute()

        assertEquals(0, count)
        coVerify(exactly = 0) { pendingDao.insert(any()) }
    }

    @Test fun `non-transaction SMS is skipped`() = runTest {
        val raw = RawSms("Your OTP is 123456", System.currentTimeMillis(), "HDFCBK")
        every { smsReader.readInbox() } returns listOf(raw)
        every { smsParser.parse(any()) } returns null

        val count = useCase.execute()
        assertEquals(0, count)
    }

    @Test fun `empty inbox returns zero`() = runTest {
        every { smsReader.readInbox() } returns emptyList()
        val count = useCase.execute()
        assertEquals(0, count)
    }
}
