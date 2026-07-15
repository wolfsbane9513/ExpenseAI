package com.expenseai.data.repository;

import com.expenseai.data.local.FireModelDao;
import com.expenseai.data.local.FireModelEntity;
import com.expenseai.data.local.LocalDateAdapter;
import com.expenseai.domain.fire.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDate;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0002J\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nJ\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/expenseai/data/repository/FireRepository;", "", "fireModelDao", "Lcom/expenseai/data/local/FireModelDao;", "(Lcom/expenseai/data/local/FireModelDao;)V", "gson", "Lcom/google/gson/Gson;", "getDefaultFireModel", "Lcom/expenseai/domain/fire/FireModel;", "getFireModel", "Lkotlinx/coroutines/flow/Flow;", "saveFireModel", "", "fireModel", "(Lcom/expenseai/domain/fire/FireModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class FireRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.local.FireModelDao fireModelDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    
    @javax.inject.Inject()
    public FireRepository(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.FireModelDao fireModelDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.expenseai.domain.fire.FireModel> getFireModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveFireModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireModel fireModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.expenseai.domain.fire.FireModel getDefaultFireModel() {
        return null;
    }
}