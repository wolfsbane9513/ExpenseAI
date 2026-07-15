package com.expenseai;

import com.expenseai.ai.GemmaModelManager;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.ModelStatus;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u0007\u001a\u00060\bj\u0002`\tH\u0007J\f\u0010\n\u001a\u00060\bj\u0002`\tH\u0007J\f\u0010\u000b\u001a\u00060\bj\u0002`\tH\u0007J\f\u0010\f\u001a\u00060\bj\u0002`\tH\u0007J\f\u0010\r\u001a\u00060\bj\u0002`\tH\u0007J\b\u0010\u000e\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/expenseai/GemmaServiceTest;", "", "()V", "modelManager", "Lcom/expenseai/ai/GemmaModelManager;", "service", "Lcom/expenseai/ai/GemmaService;", "fallback categorize identifies food keywords", "", "Lkotlinx/coroutines/test/TestResult;", "fallback categorize identifies transport keywords", "fallback insights contains total amount", "parseReceipt returns fallback when model not initialized", "parseReceipt sanitizes prompt injection in OCR text", "setup", "app_debugUnitTest"})
public final class GemmaServiceTest {
    private com.expenseai.ai.GemmaModelManager modelManager;
    private com.expenseai.ai.GemmaService service;
    
    public GemmaServiceTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
}