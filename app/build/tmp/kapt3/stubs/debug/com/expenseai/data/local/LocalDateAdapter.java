package com.expenseai.data.local;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\b\u0012\u0004\u0012\u00020\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J \u0010\b\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J \u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0012H\u0016R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/expenseai/data/local/LocalDateAdapter;", "Lcom/google/gson/JsonSerializer;", "Ljava/time/LocalDate;", "Lcom/google/gson/JsonDeserializer;", "()V", "formatter", "Ljava/time/format/DateTimeFormatter;", "kotlin.jvm.PlatformType", "deserialize", "json", "Lcom/google/gson/JsonElement;", "typeOfT", "Ljava/lang/reflect/Type;", "context", "Lcom/google/gson/JsonDeserializationContext;", "serialize", "src", "typeOfSrc", "Lcom/google/gson/JsonSerializationContext;", "app_debug"})
public final class LocalDateAdapter implements com.google.gson.JsonSerializer<java.time.LocalDate>, com.google.gson.JsonDeserializer<java.time.LocalDate> {
    private final java.time.format.DateTimeFormatter formatter = null;
    
    public LocalDateAdapter() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.google.gson.JsonElement serialize(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate src, @org.jetbrains.annotations.NotNull()
    java.lang.reflect.Type typeOfSrc, @org.jetbrains.annotations.NotNull()
    com.google.gson.JsonSerializationContext context) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.time.LocalDate deserialize(@org.jetbrains.annotations.NotNull()
    com.google.gson.JsonElement json, @org.jetbrains.annotations.NotNull()
    java.lang.reflect.Type typeOfT, @org.jetbrains.annotations.NotNull()
    com.google.gson.JsonDeserializationContext context) {
        return null;
    }
}