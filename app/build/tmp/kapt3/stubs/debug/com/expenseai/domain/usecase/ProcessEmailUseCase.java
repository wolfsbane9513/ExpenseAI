package com.expenseai.domain.usecase;

import com.expenseai.ai.EmailParser;
import com.expenseai.ai.GemmaService;
import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.data.local.PendingExpenseEntity;
import com.expenseai.security.InputSanitizer;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ \u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/expenseai/domain/usecase/ProcessEmailUseCase;", "", "emailParser", "Lcom/expenseai/ai/EmailParser;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "(Lcom/expenseai/ai/EmailParser;Lcom/expenseai/ai/GemmaService;Lcom/expenseai/data/local/PendingExpenseDao;)V", "execute", "", "body", "", "subject", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ProcessEmailUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.EmailParser emailParser = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.local.PendingExpenseDao pendingDao = null;
    
    @javax.inject.Inject()
    public ProcessEmailUseCase(@org.jetbrains.annotations.NotNull()
    com.expenseai.ai.EmailParser emailParser, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService, @org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.PendingExpenseDao pendingDao) {
        super();
    }
    
    /**
     * Parses a shared email body. Returns true if a new pending expense was staged.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object execute(@org.jetbrains.annotations.NotNull()
    java.lang.String body, @org.jetbrains.annotations.NotNull()
    java.lang.String subject, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
}