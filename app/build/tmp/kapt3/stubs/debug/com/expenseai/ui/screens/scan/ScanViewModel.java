package com.expenseai.ui.screens.scan;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.OCRService;
import com.expenseai.ai.ParsedReceipt;
import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.domain.model.Expense;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import java.time.LocalDate;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016J\u0006\u0010\u0017\u001a\u00020\u0011J\u0006\u0010\u0018\u001a\u00020\u0011J\u000e\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001bR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001f"}, d2 = {"Lcom/expenseai/ui/screens/scan/ScanViewModel;", "Landroidx/lifecycle/ViewModel;", "ocrService", "Lcom/expenseai/ai/OCRService;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "repository", "Lcom/expenseai/data/repository/ExpenseRepository;", "(Lcom/expenseai/ai/OCRService;Lcom/expenseai/ai/GemmaService;Lcom/expenseai/data/repository/ExpenseRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/expenseai/ui/screens/scan/ScanUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "processImage", "", "bitmap", "Landroid/graphics/Bitmap;", "processImageUri", "uri", "Landroid/net/Uri;", "reset", "saveExpense", "updateAmount", "value", "", "updateCategory", "updateDate", "updateVendor", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ScanViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.OCRService ocrService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.ExpenseRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.expenseai.ui.screens.scan.ScanUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.scan.ScanUiState> uiState = null;
    
    @javax.inject.Inject()
    public ScanViewModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.ai.OCRService ocrService, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService, @org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.ExpenseRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.scan.ScanUiState> getUiState() {
        return null;
    }
    
    public final void processImage(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
    }
    
    public final void processImageUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void updateVendor(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void updateAmount(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void updateCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void updateDate(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void saveExpense() {
    }
    
    public final void reset() {
    }
}