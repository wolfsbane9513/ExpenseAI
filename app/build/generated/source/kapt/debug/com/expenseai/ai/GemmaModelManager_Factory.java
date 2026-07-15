package com.expenseai.ai;

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
public final class GemmaModelManager_Factory implements Factory<GemmaModelManager> {
  private final Provider<Context> contextProvider;

  public GemmaModelManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public GemmaModelManager get() {
    return newInstance(contextProvider.get());
  }

  public static GemmaModelManager_Factory create(Provider<Context> contextProvider) {
    return new GemmaModelManager_Factory(contextProvider);
  }

  public static GemmaModelManager newInstance(Context context) {
    return new GemmaModelManager(context);
  }
}
