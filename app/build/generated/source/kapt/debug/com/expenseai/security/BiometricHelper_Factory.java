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
public final class BiometricHelper_Factory implements Factory<BiometricHelper> {
  private final Provider<Context> contextProvider;

  private final Provider<EncryptedPreferences> encryptedPreferencesProvider;

  public BiometricHelper_Factory(Provider<Context> contextProvider,
      Provider<EncryptedPreferences> encryptedPreferencesProvider) {
    this.contextProvider = contextProvider;
    this.encryptedPreferencesProvider = encryptedPreferencesProvider;
  }

  @Override
  public BiometricHelper get() {
    return newInstance(contextProvider.get(), encryptedPreferencesProvider.get());
  }

  public static BiometricHelper_Factory create(Provider<Context> contextProvider,
      Provider<EncryptedPreferences> encryptedPreferencesProvider) {
    return new BiometricHelper_Factory(contextProvider, encryptedPreferencesProvider);
  }

  public static BiometricHelper newInstance(Context context,
      EncryptedPreferences encryptedPreferences) {
    return new BiometricHelper(context, encryptedPreferences);
  }
}
