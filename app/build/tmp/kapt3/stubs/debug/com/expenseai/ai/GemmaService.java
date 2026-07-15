package com.expenseai.ai;

import android.content.Context;
import com.expenseai.security.InputSanitizer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.mediapipe.tasks.genai.llminference.LlmInference;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J$\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u00142\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00140\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000eH\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000eH\u0002J*\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u00142\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00140\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020 H\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0010\u0010$\u001a\u00020\u00182\u0006\u0010%\u001a\u00020\u000eH\u0002J\u0016\u0010&\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\'\u001a\u00020\u001b2\u0006\u0010(\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0010\u0010)\u001a\u00020\u001b2\u0006\u0010%\u001a\u00020\u000eH\u0002J\u0010\u0010*\u001a\u00020\u000e2\u0006\u0010+\u001a\u00020\u000eH\u0002J%\u0010,\u001a\u00020\n*\u00020\u000e2\u0012\u0010-\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000e0.\"\u00020\u000eH\u0002\u00a2\u0006\u0002\u0010/R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/expenseai/ai/GemmaService;", "", "context", "Landroid/content/Context;", "modelManager", "Lcom/expenseai/ai/GemmaModelManager;", "(Landroid/content/Context;Lcom/expenseai/ai/GemmaModelManager;)V", "gson", "Lcom/google/gson/Gson;", "isInitialized", "", "llmInference", "Lcom/google/mediapipe/tasks/genai/llminference/LlmInference;", "categorizeExpense", "", "description", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fallbackCategorize", "fallbackInsights", "total", "", "breakdown", "", "fallbackParseReceipt", "Lcom/expenseai/ai/ParsedReceipt;", "ocrText", "fallbackParseTransaction", "Lcom/expenseai/ai/ParsedTransaction;", "text", "generateInsights", "(DLjava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initialize", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseEmail", "emailText", "parseJsonResponse", "json", "parseReceipt", "parseSms", "smsText", "parseTransactionResponse", "runInference", "prompt", "containsAny", "keywords", "", "(Ljava/lang/String;[Ljava/lang/String;)Z", "app_debug"})
public final class GemmaService {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaModelManager modelManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.mediapipe.tasks.genai.llminference.LlmInference llmInference;
    @kotlin.jvm.Volatile()
    private volatile boolean isInitialized = false;
    
    @javax.inject.Inject()
    public GemmaService(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaModelManager modelManager) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object initialize(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object parseReceipt(@org.jetbrains.annotations.NotNull()
    java.lang.String ocrText, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expenseai.ai.ParsedReceipt> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object categorizeExpense(@org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object generateInsights(double total, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Double> breakdown, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object parseSms(@org.jetbrains.annotations.NotNull()
    java.lang.String smsText, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expenseai.ai.ParsedTransaction> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object parseEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String emailText, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expenseai.ai.ParsedTransaction> $completion) {
        return null;
    }
    
    private final java.lang.String runInference(java.lang.String prompt) {
        return null;
    }
    
    private final com.expenseai.ai.ParsedReceipt parseJsonResponse(java.lang.String json) {
        return null;
    }
    
    private final com.expenseai.ai.ParsedTransaction parseTransactionResponse(java.lang.String json) {
        return null;
    }
    
    private final com.expenseai.ai.ParsedReceipt fallbackParseReceipt(java.lang.String ocrText) {
        return null;
    }
    
    private final java.lang.String fallbackCategorize(java.lang.String description) {
        return null;
    }
    
    private final java.lang.String fallbackInsights(double total, java.util.Map<java.lang.String, java.lang.Double> breakdown) {
        return null;
    }
    
    private final com.expenseai.ai.ParsedTransaction fallbackParseTransaction(java.lang.String text) {
        return null;
    }
    
    private final boolean containsAny(java.lang.String $this$containsAny, java.lang.String... keywords) {
        return false;
    }
}