package com.expenseai.ui.screens.firemodel;

import androidx.lifecycle.ViewModel;
import com.expenseai.data.repository.FireRepository;
import com.expenseai.domain.fire.FireModel;
import com.expenseai.domain.fire.FireProfile;
import com.expenseai.domain.fire.TargetMode;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDate;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0017"}, d2 = {"Lcom/expenseai/ui/screens/firemodel/FireModelViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/expenseai/data/repository/FireRepository;", "(Lcom/expenseai/data/repository/FireRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/expenseai/ui/screens/firemodel/FireModelUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "resetSavedStatus", "", "save", "updateAnnualExpenses", "value", "", "updateEquityCagr", "updateStartCorpus", "updateTargetMode", "mode", "Lcom/expenseai/domain/fire/TargetMode;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class FireModelViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.FireRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.expenseai.ui.screens.firemodel.FireModelUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.firemodel.FireModelUiState> uiState = null;
    
    @javax.inject.Inject()
    public FireModelViewModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.FireRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.firemodel.FireModelUiState> getUiState() {
        return null;
    }
    
    public final void updateTargetMode(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.TargetMode mode) {
    }
    
    public final void updateAnnualExpenses(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void updateEquityCagr(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void updateStartCorpus(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void save() {
    }
    
    public final void resetSavedStatus() {
    }
}