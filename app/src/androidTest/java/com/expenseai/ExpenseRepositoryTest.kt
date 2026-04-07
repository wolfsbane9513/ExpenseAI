package com.expenseai

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.expenseai.data.local.ExpenseDatabase
import com.expenseai.data.local.PendingExpenseEntity
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.model.Expense
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpenseRepositoryTest {

    private lateinit var db: ExpenseDatabase
    private lateinit var repository: ExpenseRepository

    @Before fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, ExpenseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ExpenseRepository(db.expenseDao())
    }

    @After fun teardown() { db.close() }

    @Test fun `insert and retrieve expense by month`() = runTest {
        repository.addExpense(Expense(vendor = "Swiggy", amount = 350.0, category = "food", date = "2026-04-06"))
        val expenses = repository.getExpensesByMonth(2026, 4).first()
        assertEquals(1, expenses.size)
        assertEquals("Swiggy", expenses[0].vendor)
    }

    @Test fun `monthly total sums correctly`() = runTest {
        repository.addExpense(Expense(vendor = "Uber", amount = 150.0, category = "transport", date = "2026-04-05"))
        repository.addExpense(Expense(vendor = "Amazon", amount = 500.0, category = "shopping", date = "2026-04-06"))
        val total = repository.getMonthlyTotal(2026, 4).first()
        assertEquals(650.0, total, 0.01)
    }

    @Test fun `delete expense removes from query`() = runTest {
        val id = repository.addExpense(Expense(vendor = "Test", amount = 100.0, category = "other", date = "2026-04-06"))
        repository.deleteExpense(id)
        val expenses = repository.getExpensesByMonth(2026, 4).first()
        assertTrue(expenses.isEmpty())
    }

    @Test fun `pending expense dedup key prevents double insert`() = runTest {
        val dao = db.pendingExpenseDao()
        val pending = PendingExpenseEntity(
            vendor = "HDFC", amount = 500.0, category = "other",
            date = "2026-04-06", source = "sms", dedupKey = "REF123"
        )
        dao.insert(pending)
        dao.insert(pending.copy(id = 0))   // same dedupKey — IGNORE conflict
        val count = dao.getCount().first()
        assertEquals(1, count)
    }

    @Test fun `confirm pending expense moves it to expenses`() = runTest {
        val pendingDao = db.pendingExpenseDao()
        val pending = PendingExpenseEntity(
            vendor = "Zomato", amount = 250.0, category = "food",
            date = "2026-04-06", source = "sms", dedupKey = "REF456"
        )
        val pendingId = pendingDao.insert(pending)

        // Simulate confirm: add to expenses, delete from pending
        repository.addExpense(Expense(vendor = pending.vendor, amount = pending.amount,
            category = pending.category, date = pending.date, source = pending.source))
        pendingDao.deleteById(pendingId)

        val expenses = repository.getExpensesByMonth(2026, 4).first()
        assertEquals(1, expenses.size)
        assertEquals(0, pendingDao.getCount().first())
    }
}
