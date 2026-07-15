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
public final class GemmaService_Factory implements Factory<GemmaService> {
  private final Provider<Context> contextProvider;

  private final Provider<GemmaModelManager> modelManagerProvider;

  public GemmaService_Factory(Provider<Context> contextProvider,
      Provider<GemmaModelManager> modelManagerProvider) {
    this.contextProvider = contextProvider;
    this.modelManagerProvider = modelManagerProvider;
  }

  @Override
  public GemmaService get() {
    return newInstance(contextProvider.get(), modelManagerProvider.get());
  }

  public static GemmaService_Factory create(Provider<Context> contextProvider,
      Provider<GemmaModelManager> modelManagerProvider) {
    return new GemmaService_Factory(contextProvider, modelManagerProvider);
  }

  public static GemmaService newInstance(Context context, GemmaModelManager modelManager) {
    return new GemmaService(context, modelManager);
  }
}
