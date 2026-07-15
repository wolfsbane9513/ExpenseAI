package com.expenseai.ai;

import android.content.Context;
import android.net.Uri;
import dagger.hilt.android.qualifiers.ApplicationContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/expenseai/ai/SmsReader;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "readInbox", "", "Lcom/expenseai/ai/RawSms;", "days", "", "limit", "app_debug"})
public final class SmsReader {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    @javax.inject.Inject()
    public SmsReader(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Returns SMSes from the last [days] days, up to [limit].
     * Caller must verify READ_SMS permission before calling this.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.ai.RawSms> readInbox(int days, int limit) {
        return null;
    }
}