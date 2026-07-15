package com.expenseai;

import com.expenseai.ai.EmailParser;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.ParsedReceipt;
import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.domain.usecase.ProcessEmailUseCase;
import io.mockk.*;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u000b\u001a\u00060\fj\u0002`\rH\u0007J\f\u0010\u000e\u001a\u00060\fj\u0002`\rH\u0007J\b\u0010\u000f\u001a\u00020\fH\u0007J\f\u0010\u0010\u001a\u00060\fj\u0002`\rH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/expenseai/ProcessEmailUseCaseTest;", "", "()V", "emailParser", "Lcom/expenseai/ai/EmailParser;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "useCase", "Lcom/expenseai/domain/usecase/ProcessEmailUseCase;", "duplicate email is not staged again", "", "Lkotlinx/coroutines/test/TestResult;", "non-transactional email returns false", "setup", "valid transactional email stages pending expense", "app_debugUnitTest"})
public final class ProcessEmailUseCaseTest {
    private com.expenseai.ai.EmailParser emailParser;
    private com.expenseai.ai.GemmaService gemmaService;
    private com.expenseai.data.local.PendingExpenseDao pendingDao;
    private com.expenseai.domain.usecase.ProcessEmailUseCase useCase;
    
    public ProcessEmailUseCaseTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
}