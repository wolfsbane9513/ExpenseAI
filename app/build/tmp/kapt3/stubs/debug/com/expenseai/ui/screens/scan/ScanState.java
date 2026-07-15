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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2 = {"Lcom/expenseai/ui/screens/scan/ScanState;", "", "(Ljava/lang/String;I)V", "IDLE", "CAPTURING", "PROCESSING_OCR", "PROCESSING_AI", "REVIEW", "SAVING", "SAVED", "ERROR", "app_debug"})
public enum ScanState {
    /*public static final*/ IDLE /* = new IDLE() */,
    /*public static final*/ CAPTURING /* = new CAPTURING() */,
    /*public static final*/ PROCESSING_OCR /* = new PROCESSING_OCR() */,
    /*public static final*/ PROCESSING_AI /* = new PROCESSING_AI() */,
    /*public static final*/ REVIEW /* = new REVIEW() */,
    /*public static final*/ SAVING /* = new SAVING() */,
    /*public static final*/ SAVED /* = new SAVED() */,
    /*public static final*/ ERROR /* = new ERROR() */;
    
    ScanState() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.expenseai.ui.screens.scan.ScanState> getEntries() {
        return null;
    }
}