package com.expenseai.data.repository;

import com.expenseai.data.local.CategoryTotal;
import com.expenseai.data.local.ExpenseDao;
import com.expenseai.data.local.ExpenseEntity;
import com.expenseai.domain.model.Expense;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u0012\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00120\u0011J\"\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00120\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000e\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\"\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00120\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016J\u001c\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00120\u00112\b\b\u0002\u0010\u001d\u001a\u00020\u0016J\u001a\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00120\u00112\u0006\u0010\u001f\u001a\u00020 J\u0016\u0010!\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\f\u0010\"\u001a\u00020\n*\u00020#H\u0002J\f\u0010$\u001a\u00020#*\u00020\nH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/expenseai/data/repository/ExpenseRepository;", "", "expenseDao", "Lcom/expenseai/data/local/ExpenseDao;", "(Lcom/expenseai/data/local/ExpenseDao;)V", "gson", "Lcom/google/gson/Gson;", "addExpense", "", "expense", "Lcom/expenseai/domain/model/Expense;", "(Lcom/expenseai/domain/model/Expense;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteExpense", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllExpenses", "Lkotlinx/coroutines/flow/Flow;", "", "getCategoryTotals", "Lcom/expenseai/data/local/CategoryTotal;", "year", "", "month", "getExpenseById", "getExpensesByMonth", "getMonthlyTotal", "", "getRecentExpenses", "limit", "searchExpenses", "query", "", "updateExpense", "toDomain", "Lcom/expenseai/data/local/ExpenseEntity;", "toEntity", "app_debug"})
public final class ExpenseRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.local.ExpenseDao expenseDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    
    @javax.inject.Inject()
    public ExpenseRepository(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.ExpenseDao expenseDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expenseai.domain.model.Expense>> getAllExpenses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expenseai.domain.model.Expense>> getExpensesByMonth(int year, int month) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getMonthlyTotal(int year, int month) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expenseai.data.local.CategoryTotal>> getCategoryTotals(int year, int month) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expenseai.domain.model.Expense>> getRecentExpenses(int limit) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expenseai.domain.model.Expense>> searchExpenses(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getExpenseById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expenseai.domain.model.Expense> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addExpense(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.model.Expense expense, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateExpense(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.model.Expense expense, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteExpense(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.expenseai.domain.model.Expense toDomain(com.expenseai.data.local.ExpenseEntity $this$toDomain) {
        return null;
    }
    
    private final com.expenseai.data.local.ExpenseEntity toEntity(com.expenseai.domain.model.Expense $this$toEntity) {
        return null;
    }
}