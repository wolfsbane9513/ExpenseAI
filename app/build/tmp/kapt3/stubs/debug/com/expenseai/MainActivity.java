package com.expenseai;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.material.icons.Icons;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.fragment.app.FragmentActivity;
import com.expenseai.security.BiometricHelper;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\fH\u0003J\u0012\u0010\r\u001a\u00020\n2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u0010"}, d2 = {"Lcom/expenseai/MainActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "biometricHelper", "Lcom/expenseai/security/BiometricHelper;", "getBiometricHelper", "()Lcom/expenseai/security/BiometricHelper;", "setBiometricHelper", "(Lcom/expenseai/security/BiometricHelper;)V", "LockScreen", "", "onAuthSuccess", "Lkotlin/Function0;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class MainActivity extends androidx.fragment.app.FragmentActivity {
    @javax.inject.Inject()
    public com.expenseai.security.BiometricHelper biometricHelper;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.security.BiometricHelper getBiometricHelper() {
        return null;
    }
    
    public final void setBiometricHelper(@org.jetbrains.annotations.NotNull()
    com.expenseai.security.BiometricHelper p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @androidx.compose.runtime.Composable()
    private final void LockScreen(kotlin.jvm.functions.Function0<kotlin.Unit> onAuthSuccess) {
    }
}