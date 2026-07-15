package com.expenseai;

import com.expenseai.ai.SmsParser;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0010\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\u0006H\u0007J\b\u0010\b\u001a\u00020\u0006H\u0007J\b\u0010\t\u001a\u00020\u0006H\u0007J\b\u0010\n\u001a\u00020\u0006H\u0007J\b\u0010\u000b\u001a\u00020\u0006H\u0007J\b\u0010\f\u001a\u00020\u0006H\u0007J\b\u0010\r\u001a\u00020\u0006H\u0007J\b\u0010\u000e\u001a\u00020\u0006H\u0007J\b\u0010\u000f\u001a\u00020\u0006H\u0007J\b\u0010\u0010\u001a\u00020\u0006H\u0007J\b\u0010\u0011\u001a\u00020\u0006H\u0007J\b\u0010\u0012\u001a\u00020\u0006H\u0007J\b\u0010\u0013\u001a\u00020\u0006H\u0007J\b\u0010\u0014\u001a\u00020\u0006H\u0007J\b\u0010\u0015\u001a\u00020\u0006H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/expenseai/SmsParserTest;", "", "()V", "parser", "Lcom/expenseai/ai/SmsParser;", "dedupKey differs for different messages", "", "dedupKey is stable for same message", "extracts date in dd-MM-yy format", "extracts date in dd-MM-yyyy format", "extracts date in dd-Mon-yy format", "parses Axis UPI debit", "parses HDFC debit alert", "parses ICICI credit card spend", "parses Kotak net banking transfer", "parses SBI ATM withdrawal", "parses amount with comma separators", "parses rupee symbol amount", "parses whole number amount", "returns null for OTP message", "returns null for promotional message", "setup", "app_debugUnitTest"})
public final class SmsParserTest {
    private com.expenseai.ai.SmsParser parser;
    
    public SmsParserTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
}