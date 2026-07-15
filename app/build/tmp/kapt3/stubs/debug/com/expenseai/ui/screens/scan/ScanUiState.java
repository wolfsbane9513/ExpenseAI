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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bc\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u0010\"\u001a\u00020\u0005H\u00c6\u0003J\t\u0010#\u001a\u00020\u0005H\u00c6\u0003J\t\u0010$\u001a\u00020\u0005H\u00c6\u0003J\t\u0010%\u001a\u00020\u0005H\u00c6\u0003Jg\u0010&\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010*\u001a\u00020+H\u00d6\u0001J\t\u0010,\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006-"}, d2 = {"Lcom/expenseai/ui/screens/scan/ScanUiState;", "", "scanState", "Lcom/expenseai/ui/screens/scan/ScanState;", "ocrText", "", "parsedReceipt", "Lcom/expenseai/ai/ParsedReceipt;", "errorMessage", "capturedImageUri", "Landroid/net/Uri;", "editVendor", "editAmount", "editCategory", "editDate", "(Lcom/expenseai/ui/screens/scan/ScanState;Ljava/lang/String;Lcom/expenseai/ai/ParsedReceipt;Ljava/lang/String;Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getCapturedImageUri", "()Landroid/net/Uri;", "getEditAmount", "()Ljava/lang/String;", "getEditCategory", "getEditDate", "getEditVendor", "getErrorMessage", "getOcrText", "getParsedReceipt", "()Lcom/expenseai/ai/ParsedReceipt;", "getScanState", "()Lcom/expenseai/ui/screens/scan/ScanState;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class ScanUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ui.screens.scan.ScanState scanState = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String ocrText = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.ParsedReceipt parsedReceipt = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    @org.jetbrains.annotations.Nullable()
    private final android.net.Uri capturedImageUri = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String editVendor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String editAmount = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String editCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String editDate = null;
    
    public ScanUiState(@org.jetbrains.annotations.NotNull()
    com.expenseai.ui.screens.scan.ScanState scanState, @org.jetbrains.annotations.NotNull()
    java.lang.String ocrText, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.ParsedReceipt parsedReceipt, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.Nullable()
    android.net.Uri capturedImageUri, @org.jetbrains.annotations.NotNull()
    java.lang.String editVendor, @org.jetbrains.annotations.NotNull()
    java.lang.String editAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String editCategory, @org.jetbrains.annotations.NotNull()
    java.lang.String editDate) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ui.screens.scan.ScanState getScanState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOcrText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ai.ParsedReceipt getParsedReceipt() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.net.Uri getCapturedImageUri() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEditVendor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEditAmount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEditCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEditDate() {
        return null;
    }
    
    public ScanUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ui.screens.scan.ScanState component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ai.ParsedReceipt component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.net.Uri component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ui.screens.scan.ScanUiState copy(@org.jetbrains.annotations.NotNull()
    com.expenseai.ui.screens.scan.ScanState scanState, @org.jetbrains.annotations.NotNull()
    java.lang.String ocrText, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.ParsedReceipt parsedReceipt, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.Nullable()
    android.net.Uri capturedImageUri, @org.jetbrains.annotations.NotNull()
    java.lang.String editVendor, @org.jetbrains.annotations.NotNull()
    java.lang.String editAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String editCategory, @org.jetbrains.annotations.NotNull()
    java.lang.String editDate) {
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