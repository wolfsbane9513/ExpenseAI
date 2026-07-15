package com.expenseai.ui.screens.scan;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.core.content.FileProvider;
import androidx.core.content.ContextCompat;
import java.io.File;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001ah\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a\u0012\u0010\f\u001a\u00020\u00012\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u00a8\u0006\u000f"}, d2 = {"ReviewForm", "", "uiState", "Lcom/expenseai/ui/screens/scan/ScanUiState;", "onVendorChange", "Lkotlin/Function1;", "", "onAmountChange", "onCategoryChange", "onSave", "Lkotlin/Function0;", "onRetry", "ScanReceiptScreen", "viewModel", "Lcom/expenseai/ui/screens/scan/ScanViewModel;", "app_debug"})
public final class ScanReceiptScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ScanReceiptScreen(@org.jetbrains.annotations.NotNull()
    com.expenseai.ui.screens.scan.ScanViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void ReviewForm(com.expenseai.ui.screens.scan.ScanUiState uiState, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onVendorChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAmountChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCategoryChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onRetry) {
    }
}