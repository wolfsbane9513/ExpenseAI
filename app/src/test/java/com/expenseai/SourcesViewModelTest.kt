package com.expenseai

import app.cash.turbine.test
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.ui.screens.sources.SourcesViewModel
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourcesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var pendingDao: PendingExpenseDao
    private lateinit var viewModel: SourcesViewModel
    private val countFlow = MutableStateFlow(0)

    @Before fun setup() {
        Dispatchers.setMain(testDispatcher)
        pendingDao = mockk()
        every { pendingDao.getCount() } returns countFlow
        viewModel = SourcesViewModel(pendingDao)
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
}
