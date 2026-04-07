package com.expenseai

import app.cash.turbine.test
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.data.local.PendingExpenseEntity
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.usecase.ProcessEmailUseCase
import com.expenseai.ui.screens.review.ReviewViewModel
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
class ReviewViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var pendingDao: PendingExpenseDao
    private lateinit var repository: ExpenseRepository
    private lateinit var processEmailUseCase: ProcessEmailUseCase
    private lateinit var viewModel: ReviewViewModel

    private val pendingFlow = MutableStateFlow<List<PendingExpenseEntity>>(emptyList())

    @Before fun setup() {
        Dispatchers.setMain(testDispatcher)
        pendingDao = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        processEmailUseCase = mockk(relaxed = true)
        every { pendingDao.getAll() } returns pendingFlow
        viewModel = ReviewViewModel(pendingDao, repository, processEmailUseCase)
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
}
