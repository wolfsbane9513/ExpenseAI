package com.expenseai.security;

/**
 * Input sanitization utility to prevent injection attacks
 * in OCR text, user inputs, and LLM prompt construction.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\rJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rJ\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\rJ\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\rJ\u000e\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rJ\u000e\u0010\u0017\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\rJ\u000e\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\rJ\u000e\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\rR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/expenseai/security/InputSanitizer;", "", "()V", "MAX_AMOUNT_LENGTH", "", "MAX_EMAIL_LENGTH", "MAX_OCR_LENGTH", "MAX_SMS_LENGTH", "MAX_TEXT_LENGTH", "MAX_VENDOR_LENGTH", "isValidDate", "", "date", "", "sanitizeAmountInput", "input", "sanitizeEmailText", "raw", "sanitizeFilePath", "path", "sanitizeOcrText", "ocrText", "sanitizeSmsText", "sanitizeTextInput", "sanitizeVendorName", "vendor", "validateCategory", "category", "app_debug"})
public final class InputSanitizer {
    private static final int MAX_TEXT_LENGTH = 500;
    private static final int MAX_AMOUNT_LENGTH = 15;
    private static final int MAX_OCR_LENGTH = 5000;
    private static final int MAX_VENDOR_LENGTH = 100;
    private static final int MAX_SMS_LENGTH = 500;
    private static final int MAX_EMAIL_LENGTH = 2000;
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.security.InputSanitizer INSTANCE = null;
    
    private InputSanitizer() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitizeTextInput(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitizeAmountInput(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitizeOcrText(@org.jetbrains.annotations.NotNull()
    java.lang.String ocrText) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitizeSmsText(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitizeEmailText(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitizeVendorName(@org.jetbrains.annotations.NotNull()
    java.lang.String vendor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String validateCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category) {
        return null;
    }
    
    public final boolean isValidDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitizeFilePath(@org.jetbrains.annotations.NotNull()
    java.lang.String path) {
        return null;
    }
}