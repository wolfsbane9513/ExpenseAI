package com.expenseai

import com.expenseai.ai.EmailParser
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ParsedReceipt
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.domain.usecase.ProcessEmailUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProcessEmailUseCaseTest {

    private lateinit var emailParser: EmailParser
    private lateinit var gemmaService: GemmaService
    private lateinit var pendingDao: PendingExpenseDao
    private lateinit var useCase: ProcessEmailUseCase

    @Before fun setup() {
        emailParser = mockk()
        gemmaService = mockk(relaxed = true)
        pendingDao = mockk(relaxed = true)
        useCase = ProcessEmailUseCase(emailParser, gemmaService, pendingDao)
    }

    @Test fun `valid transactional email stages pending expense`() = runTest {
        every { emailParser.parse(any(), any()) } returns ParsedReceipt("Amazon", 1299.0, "2026-04-06", "shopping")
        coEvery { pendingDao.countByDedupKey(any()) } returns 0
        coEvery { pendingDao.insert(any()) } returns 1L

        val result = useCase.execute(body = "Order Total Rs 1299", subject = "Your Amazon order")

        assertTrue(result)
        coVerify(exactly = 1) { pendingDao.insert(any()) }
    }

    @Test fun `non-transactional email returns false`() = runTest {
        every { emailParser.parse(any(), any()) } returns null
        val result = useCase.execute(body = "Hi friend!", subject = "Hello")
        assertFalse(result)
        coVerify(exactly = 0) { pendingDao.insert(any()) }
    }

    @Test fun `duplicate email is not staged again`() = runTest {
        every { emailParser.parse(any(), any()) } returns ParsedReceipt("Swiggy", 250.0, "2026-04-06", "food")
        coEvery { pendingDao.countByDedupKey(any()) } returns 1
        val result = useCase.execute(body = "Order #ABC Total Rs 250", subject = "Swiggy receipt")
        assertFalse(result)
        coVerify(exactly = 0) { pendingDao.insert(any()) }
    }
}
