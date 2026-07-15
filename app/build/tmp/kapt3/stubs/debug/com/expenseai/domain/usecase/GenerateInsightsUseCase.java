package com.expenseai.domain.usecase;

import com.expenseai.ai.GemmaService;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J*\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\b0\nH\u0086B\u00a2\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/expenseai/domain/usecase/GenerateInsightsUseCase;", "", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "(Lcom/expenseai/ai/GemmaService;)V", "invoke", "", "total", "", "breakdown", "", "(DLjava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class GenerateInsightsUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    
    @javax.inject.Inject()
    public GenerateInsightsUseCase(@org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invoke(double total, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Double> breakdown, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
}