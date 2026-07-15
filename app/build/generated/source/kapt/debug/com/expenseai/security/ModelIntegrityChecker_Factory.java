package com.expenseai.security;

import com.expenseai.ai.GemmaModelManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class ModelIntegrityChecker_Factory implements Factory<ModelIntegrityChecker> {
  private final Provider<GemmaModelManager> modelManagerProvider;

  private final Provider<EncryptedPreferences> encryptedPreferencesProvider;

  public ModelIntegrityChecker_Factory(Provider<GemmaModelManager> modelManagerProvider,
      Provider<EncryptedPreferences> encryptedPreferencesProvider) {
    this.modelManagerProvider = modelManagerProvider;
    this.encryptedPreferencesProvider = encryptedPreferencesProvider;
  }

  @Override
  public ModelIntegrityChecker get() {
    return newInstance(modelManagerProvider.get(), encryptedPreferencesProvider.get());
  }

  public static ModelIntegrityChecker_Factory create(
      Provider<GemmaModelManager> modelManagerProvider,
      Provider<EncryptedPreferences> encryptedPreferencesProvider) {
    return new ModelIntegrityChecker_Factory(modelManagerProvider, encryptedPreferencesProvider);
  }

  public static ModelIntegrityChecker newInstance(GemmaModelManager modelManager,
      EncryptedPreferences encryptedPreferences) {
    return new ModelIntegrityChecker(modelManager, encryptedPreferences);
  }
}
