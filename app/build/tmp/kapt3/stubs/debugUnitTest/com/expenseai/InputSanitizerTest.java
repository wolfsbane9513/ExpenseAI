package com.expenseai;

import com.expenseai.security.InputSanitizer;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007J\b\u0010\u0006\u001a\u00020\u0004H\u0007J\b\u0010\u0007\u001a\u00020\u0004H\u0007J\b\u0010\b\u001a\u00020\u0004H\u0007J\b\u0010\t\u001a\u00020\u0004H\u0007J\b\u0010\n\u001a\u00020\u0004H\u0007\u00a8\u0006\u000b"}, d2 = {"Lcom/expenseai/InputSanitizerTest;", "", "()V", "sanitizeEmailText strips HTML tags", "", "sanitizeEmailText strips prompt injection markers", "sanitizeEmailText truncates to 2000 chars", "sanitizeSmsText strips HTML tags", "sanitizeSmsText strips control characters", "sanitizeSmsText strips prompt injection markers", "sanitizeSmsText truncates to 500 chars", "app_debugUnitTest"})
public final class InputSanitizerTest {
    
    public InputSanitizerTest() {
        super();
    }
}