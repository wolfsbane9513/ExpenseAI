package com.expenseai;

import com.expenseai.ai.GemmaService;
import com.expenseai.ai.ParsedReceipt;
import com.expenseai.ai.RawSms;
import com.expenseai.ai.SmsParser;
import com.expenseai.ai.SmsReader;
import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.domain.usecase.ProcessSmsUseCase;
import io.mockk.*;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\r\u001a\u00060\u000ej\u0002`\u000fH\u0007J\f\u0010\u0010\u001a\u00060\u000ej\u0002`\u000fH\u0007J\f\u0010\u0011\u001a\u00060\u000ej\u0002`\u000fH\u0007J\b\u0010\u0012\u001a\u00020\u000eH\u0007J\f\u0010\u0013\u001a\u00060\u000ej\u0002`\u000fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/expenseai/ProcessSmsUseCaseTest;", "", "()V", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "smsParser", "Lcom/expenseai/ai/SmsParser;", "smsReader", "Lcom/expenseai/ai/SmsReader;", "useCase", "Lcom/expenseai/domain/usecase/ProcessSmsUseCase;", "duplicate dedup key is not inserted again", "", "Lkotlinx/coroutines/test/TestResult;", "empty inbox returns zero", "non-transaction SMS is skipped", "setup", "staging new transaction SMS inserts into pending dao", "app_debugUnitTest"})
public final class ProcessSmsUseCaseTest {
    private com.expenseai.ai.SmsReader smsReader;
    private com.expenseai.ai.SmsParser smsParser;
    private com.expenseai.ai.GemmaService gemmaService;
    private com.expenseai.data.local.PendingExpenseDao pendingDao;
    private com.expenseai.domain.usecase.ProcessSmsUseCase useCase;
    
    public ProcessSmsUseCaseTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
}