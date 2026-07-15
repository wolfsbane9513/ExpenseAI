package com.expenseai.security;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class EncryptedPreferences_Factory implements Factory<EncryptedPreferences> {
  private final Provider<Context> contextProvider;

  public EncryptedPreferences_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public EncryptedPreferences get() {
    return newInstance(contextProvider.get());
  }

  public static EncryptedPreferences_Factory create(Provider<Context> contextProvider) {
    return new EncryptedPreferences_Factory(contextProvider);
  }

  public static EncryptedPreferences newInstance(Context context) {
    return new EncryptedPreferences(context);
  }
}
