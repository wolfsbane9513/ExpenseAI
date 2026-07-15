package com.expenseai.di;

import android.content.Context;
import androidx.room.Room;
import com.expenseai.data.local.ExpenseDao;
import com.expenseai.data.local.ExpenseDatabase;
import com.expenseai.data.local.PendingExpenseDao;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import net.sqlcipher.database.SupportFactory;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0012\u0010\u000b\u001a\u00020\n2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\f\u001a\u00020\rH\u0007J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\nH\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/expenseai/di/AppModule;", "", "()V", "getOrCreateDatabaseKey", "", "context", "Landroid/content/Context;", "provideExpenseDao", "Lcom/expenseai/data/local/ExpenseDao;", "database", "Lcom/expenseai/data/local/ExpenseDatabase;", "provideExpenseDatabase", "provideFireEngine", "Lcom/expenseai/domain/fire/FireEngine;", "provideFireModelDao", "Lcom/expenseai/data/local/FireModelDao;", "providePendingExpenseDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.data.local.ExpenseDatabase provideExpenseDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.data.local.FireModelDao provideFireModelDao(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.ExpenseDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.data.local.ExpenseDao provideExpenseDao(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.ExpenseDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.data.local.PendingExpenseDao providePendingExpenseDao(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.ExpenseDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.FireEngine provideFireEngine() {
        return null;
    }
    
    private final byte[] getOrCreateDatabaseKey(android.content.Context context) {
        return null;
    }
}