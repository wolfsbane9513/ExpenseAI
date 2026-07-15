package com.expenseai.ui.screens.dashboard;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.ExposedDropdownMenuDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.tooling.preview.Preview;
import com.expenseai.ai.ModelStatus;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a<\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032$\u0010\u0004\u001a \u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u009c\u0001\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032$\u0010\r\u001a \u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u0006H\u0007\u001a\u0018\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0006H\u0003\u001a\b\u0010\u0016\u001a\u00020\u0001H\u0007\u001a2\u0010\u0017\u001a\u00020\u00012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0007\u001a4\u0010\u001a\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00062\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a4\u0010\u001b\u001a\u00020\u00012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0003\u00a8\u0006 "}, d2 = {"AddExpenseDialog", "", "onDismiss", "Lkotlin/Function0;", "onConfirm", "Lkotlin/Function4;", "", "", "DashboardContent", "uiState", "Lcom/expenseai/ui/screens/dashboard/DashboardUiState;", "onPreviousMonth", "onNextMonth", "onAddExpense", "onScanClick", "onSettingsClick", "onImportModel", "onRemoveModel", "modelImportSummary", "DashboardHeroCard", "totalSpending", "budgetSummary", "DashboardPreview", "DashboardScreen", "viewModel", "Lcom/expenseai/ui/screens/dashboard/DashboardViewModel;", "ModelSetupCard", "QuickActionsCard", "onInstallModel", "hasInstalledModel", "", "isModelBusy", "app_debug"})
public final class DashboardScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DashboardScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onScanClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSettingsClick, @org.jetbrains.annotations.NotNull()
    com.expenseai.ui.screens.dashboard.DashboardViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DashboardContent(@org.jetbrains.annotations.NotNull()
    com.expenseai.ui.screens.dashboard.DashboardUiState uiState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPreviousMonth, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNextMonth, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.Double, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onAddExpense, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onScanClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSettingsClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onImportModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRemoveModel, @org.jetbrains.annotations.NotNull()
    java.lang.String modelImportSummary) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DashboardHeroCard(java.lang.String totalSpending, java.lang.String budgetSummary) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QuickActionsCard(kotlin.jvm.functions.Function0<kotlin.Unit> onScanClick, kotlin.jvm.functions.Function0<kotlin.Unit> onInstallModel, boolean hasInstalledModel, boolean isModelBusy) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModelSetupCard(com.expenseai.ui.screens.dashboard.DashboardUiState uiState, java.lang.String modelImportSummary, kotlin.jvm.functions.Function0<kotlin.Unit> onImportModel, kotlin.jvm.functions.Function0<kotlin.Unit> onRemoveModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AddExpenseDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.Double, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onConfirm) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
    @androidx.compose.runtime.Composable()
    public static final void DashboardPreview() {
    }
}