package com.expenseai.domain.usecase;

import com.expenseai.ai.EmailParser;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.ParsedReceipt;
import com.expenseai.ai.SmsParser;
import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.data.local.PendingExpenseEntity;
import com.expenseai.security.InputSanitizer;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u0000 \u001a2\u00020\u0001:\u0002\u001a\u001bB\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ>\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000e2\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0013H\u0002J \u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/expenseai/domain/usecase/ProcessSharedTextUseCase;", "", "emailParser", "Lcom/expenseai/ai/EmailParser;", "smsParser", "Lcom/expenseai/ai/SmsParser;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "(Lcom/expenseai/ai/EmailParser;Lcom/expenseai/ai/SmsParser;Lcom/expenseai/ai/GemmaService;Lcom/expenseai/data/local/PendingExpenseDao;)V", "chooseCandidate", "Lcom/expenseai/domain/usecase/ProcessSharedTextUseCase$SharedCandidate;", "rawBody", "", "subject", "emailBody", "smsBody", "emailParsed", "Lcom/expenseai/ai/ParsedReceipt;", "smsParsed", "execute", "", "body", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "looksLikeSms", "Companion", "SharedCandidate", "app_debug"})
public final class ProcessSharedTextUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.EmailParser emailParser = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.SmsParser smsParser = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.local.PendingExpenseDao pendingDao = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> smsMarkers = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.domain.usecase.ProcessSharedTextUseCase.Companion Companion = null;
    
    @javax.inject.Inject()
    public ProcessSharedTextUseCase(@org.jetbrains.annotations.NotNull()
    com.expenseai.ai.EmailParser emailParser, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.SmsParser smsParser, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService, @org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.PendingExpenseDao pendingDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object execute(@org.jetbrains.annotations.NotNull()
    java.lang.String body, @org.jetbrains.annotations.NotNull()
    java.lang.String subject, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final com.expenseai.domain.usecase.ProcessSharedTextUseCase.SharedCandidate chooseCandidate(java.lang.String rawBody, java.lang.String subject, java.lang.String emailBody, java.lang.String smsBody, com.expenseai.ai.ParsedReceipt emailParsed, com.expenseai.ai.ParsedReceipt smsParsed) {
        return null;
    }
    
    private final boolean looksLikeSms(java.lang.String body, java.lang.String subject) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/expenseai/domain/usecase/ProcessSharedTextUseCase$Companion;", "", "()V", "smsMarkers", "", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u00a8\u0006\u001a"}, d2 = {"Lcom/expenseai/domain/usecase/ProcessSharedTextUseCase$SharedCandidate;", "", "parsed", "Lcom/expenseai/ai/ParsedReceipt;", "source", "", "dedupKey", "rawText", "(Lcom/expenseai/ai/ParsedReceipt;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDedupKey", "()Ljava/lang/String;", "getParsed", "()Lcom/expenseai/ai/ParsedReceipt;", "getRawText", "getSource", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class SharedCandidate {
        @org.jetbrains.annotations.NotNull()
        private final com.expenseai.ai.ParsedReceipt parsed = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String source = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String dedupKey = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String rawText = null;
        
        public SharedCandidate(@org.jetbrains.annotations.NotNull()
        com.expenseai.ai.ParsedReceipt parsed, @org.jetbrains.annotations.NotNull()
        java.lang.String source, @org.jetbrains.annotations.NotNull()
        java.lang.String dedupKey, @org.jetbrains.annotations.NotNull()
        java.lang.String rawText) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.expenseai.ai.ParsedReceipt getParsed() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSource() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDedupKey() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getRawText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.expenseai.ai.ParsedReceipt component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.expenseai.domain.usecase.ProcessSharedTextUseCase.SharedCandidate copy(@org.jetbrains.annotations.NotNull()
        com.expenseai.ai.ParsedReceipt parsed, @org.jetbrains.annotations.NotNull()
        java.lang.String source, @org.jetbrains.annotations.NotNull()
        java.lang.String dedupKey, @org.jetbrains.annotations.NotNull()
        java.lang.String rawText) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}