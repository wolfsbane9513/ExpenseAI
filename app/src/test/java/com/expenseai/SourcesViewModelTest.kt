package com.expenseai

import app.cash.turbine.test
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.domain.usecase.ProcessSmsUseCase
import com.expenseai.ui.screens.sources.SourcesViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourcesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var processSmsUseCase: ProcessSmsUseCase
    private lateinit var pendingDao: PendingExpenseDao
    private lateinit var viewModel: SourcesViewModel
    private val countFlow = MutableStateFlow(0)

    @Before fun setup() {
        Dispatchers.setMain(testDispatcher)
        processSmsUseCase = mockk(relaxed = true)
        pendingDao = mockk()
        every { pendingDao.getCount() } returns countFlow
        viewModel = SourcesViewModel(processSmsUseCase, pendingDao)
    }

    @After fun teardown() { Dispatchers.resetMain() }

    @Test fun `pending count reflects dao flow`() = runTest {
        viewModel.pendingCount.test {
            assertEquals(0, awaitItem())
            countFlow.value = 3
            assertEquals(3, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `sms enabled defaults to false`() {
        assertFalse(viewModel.smsEnabled.value)
    }

    @Test fun `scanning triggers processSmsUseCase`() = runTest {
        coEvery { processSmsUseCase.execute() } returns 2
        viewModel.scanSms()
        coVerify(exactly = 1) { processSmsUseCase.execute() }
    }

    @Test fun `scan result updates last scan count`() = runTest {
        coEvery { processSmsUseCase.execute() } returns 5
        viewModel.scanSms()
        assertEquals(5, viewModel.lastScanCount.value)
    }
}
