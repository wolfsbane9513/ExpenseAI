package com.expenseai.ai;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0017\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0016\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0002J\u0018\u0010\f\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0006H\u0002J\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u000b\u001a\u00020\u00062\b\b\u0002\u0010\r\u001a\u00020\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/expenseai/ai/EmailParser;", "", "()V", "extractAmount", "", "text", "", "(Ljava/lang/String;)Ljava/lang/Double;", "extractDate", "extractItems", "", "body", "extractVendor", "subject", "parse", "Lcom/expenseai/ai/ParsedReceipt;", "Companion", "app_debug"})
public final class EmailParser {
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.ai.EmailParser.Companion Companion = null;
    
    @javax.inject.Inject()
    public EmailParser() {
        super();
    }
    
    /**
     * Returns null if no financial amount can be found — indicates non-transactional email.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.expenseai.ai.ParsedReceipt parse(@org.jetbrains.annotations.NotNull()
    java.lang.String body, @org.jetbrains.annotations.NotNull()
    java.lang.String subject) {
        return null;
    }
    
    private final java.lang.Double extractAmount(java.lang.String text) {
        return null;
    }
    
    private final java.lang.String extractVendor(java.lang.String body, java.lang.String subject) {
        return null;
    }
    
    private final java.lang.String extractDate(java.lang.String text) {
        return null;
    }
    
    private final java.util.List<java.lang.String> extractItems(java.lang.String body) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004\u00a8\u0006\u0007"}, d2 = {"Lcom/expenseai/ai/EmailParser$Companion;", "", "()V", "dedupKey", "", "body", "subject", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String dedupKey(@org.jetbrains.annotations.NotNull()
        java.lang.String body, @org.jetbrains.annotations.NotNull()
        java.lang.String subject) {
            return null;
        }
    }
}