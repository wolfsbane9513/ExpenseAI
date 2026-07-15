package com.expenseai.ai;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010$\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\"\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000e0\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/expenseai/ai/PromptTemplates;", "", "()V", "categorizationPrompt", "", "description", "emailParsingPrompt", "emailText", "receiptParsingPrompt", "ocrText", "smsParsingPrompt", "smsText", "spendingInsightsPrompt", "total", "", "categoryBreakdown", "", "app_debug"})
public final class PromptTemplates {
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.ai.PromptTemplates INSTANCE = null;
    
    private PromptTemplates() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String receiptParsingPrompt(@org.jetbrains.annotations.NotNull()
    java.lang.String ocrText) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String categorizationPrompt(@org.jetbrains.annotations.NotNull()
    java.lang.String description) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String spendingInsightsPrompt(double total, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Double> categoryBreakdown) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String smsParsingPrompt(@org.jetbrains.annotations.NotNull()
    java.lang.String smsText) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String emailParsingPrompt(@org.jetbrains.annotations.NotNull()
    java.lang.String emailText) {
        return null;
    }
}