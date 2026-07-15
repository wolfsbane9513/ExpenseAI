package com.expenseai.domain.usecase;

import android.graphics.Bitmap;
import android.net.Uri;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.OCRService;
import com.expenseai.ai.ParsedReceipt;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/expenseai/domain/usecase/ProcessReceiptUseCase;", "", "ocrService", "Lcom/expenseai/ai/OCRService;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "(Lcom/expenseai/ai/OCRService;Lcom/expenseai/ai/GemmaService;)V", "fromBitmap", "Lcom/expenseai/ai/ParsedReceipt;", "bitmap", "Landroid/graphics/Bitmap;", "(Landroid/graphics/Bitmap;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fromUri", "uri", "Landroid/net/Uri;", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ProcessReceiptUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.OCRService ocrService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    
    @javax.inject.Inject()
    public ProcessReceiptUseCase(@org.jetbrains.annotations.NotNull()
    com.expenseai.ai.OCRService ocrService, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fromBitmap(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expenseai.ai.ParsedReceipt> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fromUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expenseai.ai.ParsedReceipt> $completion) {
        return null;
    }
}