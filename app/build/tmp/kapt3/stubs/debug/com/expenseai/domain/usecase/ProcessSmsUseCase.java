package com.expenseai.domain.usecase;

import com.expenseai.ai.GemmaService;
import com.expenseai.ai.SmsParser;
import com.expenseai.ai.SmsReader;
import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.data.local.PendingExpenseEntity;
import com.expenseai.security.InputSanitizer;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/expenseai/domain/usecase/ProcessSmsUseCase;", "", "smsReader", "Lcom/expenseai/ai/SmsReader;", "smsParser", "Lcom/expenseai/ai/SmsParser;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "(Lcom/expenseai/ai/SmsReader;Lcom/expenseai/ai/SmsParser;Lcom/expenseai/ai/GemmaService;Lcom/expenseai/data/local/PendingExpenseDao;)V", "execute", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ProcessSmsUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.SmsReader smsReader = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.SmsParser smsParser = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.local.PendingExpenseDao pendingDao = null;
    
    @javax.inject.Inject()
    public ProcessSmsUseCase(@org.jetbrains.annotations.NotNull()
    com.expenseai.ai.SmsReader smsReader, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.SmsParser smsParser, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService, @org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.PendingExpenseDao pendingDao) {
        super();
    }
    
    /**
     * Reads SMS inbox, parses transactions, stages new ones as pending.
     * Returns the count of newly staged expenses.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object execute(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
}