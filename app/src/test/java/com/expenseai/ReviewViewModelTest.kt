package com.expenseai

import app.cash.turbine.test
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.data.local.PendingExpenseEntity
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.usecase.ProcessSharedTextUseCase
import com.expenseai.ui.screens.review.ReviewViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var pendingDao: PendingExpenseDao
    private lateinit var repository: ExpenseRepository
    private lateinit var processSharedTextUseCase: ProcessSharedTextUseCase
    private lateinit var viewModel: ReviewViewModel

    private val pendingFlow = MutableStateFlow<List<PendingExpenseEntity>>(emptyList())

    @Before fun setup() {
        Dispatchers.setMain(testDispatcher)
        pendingDao = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        processSharedTextUseCase = mockk(relaxed = true)
        every { pendingDao.getAll() } returns pendingFlow
        viewModel = ReviewViewModel(pendingDao, repository, processSharedTextUseCase)
    }

    @After fun teardown() { Dispatchers.resetMain() }

    @Test fun `pending list emits from dao`() = runTest {
        val item = PendingExpenseEntity(id = 1, vendor = "Swiggy", amount = 250.0,
            category = "food", date = "2026-04-06", source = "sms", dedupKey = "K1")
        viewModel.pendingExpenses.test {
            assertEquals(emptyList<PendingExpenseEntity>(), awaitItem())
            pendingFlow.value = listOf(item)
            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `confirm moves pending to expenses`() = runTest {
        val item = PendingExpenseEntity(id = 1, vendor = "Amazon", amount = 1299.0,
            category = "shopping", date = "2026-04-06", source = "email", dedupKey = "K2")
        viewModel.confirm(item)
        coVerify { repository.addExpense(any()) }
        coVerify { pendingDao.deleteById(1) }
    }

    @Test fun `reject deletes from pending dao`() = runTest {
        val item = PendingExpenseEntity(id = 2, vendor = "Test", amount = 100.0,
            category = "other", date = "2026-04-06", source = "sms", dedupKey = "K3")
        viewModel.reject(item)
        coVerify { pendingDao.deleteById(2) }
        coVerify(exactly = 0) { repository.addExpense(any()) }
    }

    @Test fun `shared text failure updates error state`() = runTest {
        coEvery { processSharedTextUseCase.execute(any(), any()) } returns false

        viewModel.processSharedText("not a transaction", "")

        assertFalse(viewModel.shareStagingState.value.isProcessing)
        assertTrue(viewModel.shareStagingState.value.errorMessage?.contains("couldn't stage") == true)
    }
}
