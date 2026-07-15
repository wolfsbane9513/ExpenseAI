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
public final class OCRService_Factory implements Factory<OCRService> {
  private final Provider<Context> contextProvider;

  public OCRService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public OCRService get() {
    return newInstance(contextProvider.get());
  }

  public static OCRService_Factory create(Provider<Context> contextProvider) {
    return new OCRService_Factory(contextProvider);
  }

  public static OCRService newInstance(Context context) {
    return new OCRService(context);
  }
}
