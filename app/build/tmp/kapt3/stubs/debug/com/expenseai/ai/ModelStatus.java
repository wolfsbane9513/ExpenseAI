package com.expenseai.ai;

import android.content.Context;
import android.net.Uri;
import android.provider.OpenableColumns;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.StateFlow;
import java.io.File;
import java.io.FileOutputStream;
import javax.inject.Inject;
import javax.inject.Singleton;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/expenseai/ai/ModelStatus;", "", "(Ljava/lang/String;I)V", "NOT_DOWNLOADED", "DOWNLOADING", "LOADING", "READY", "ERROR", "app_debug"})
public enum ModelStatus {
    /*public static final*/ NOT_DOWNLOADED /* = new NOT_DOWNLOADED() */,
    /*public static final*/ DOWNLOADING /* = new DOWNLOADING() */,
    /*public static final*/ LOADING /* = new LOADING() */,
    /*public static final*/ READY /* = new READY() */,
    /*public static final*/ ERROR /* = new ERROR() */;
    
    ModelStatus() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.expenseai.ai.ModelStatus> getEntries() {
        return null;
    }
}