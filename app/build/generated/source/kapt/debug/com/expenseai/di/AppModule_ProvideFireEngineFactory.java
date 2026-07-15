package com.expenseai.di;

import com.expenseai.domain.fire.FireEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AppModule_ProvideFireEngineFactory implements Factory<FireEngine> {
  @Override
  public FireEngine get() {
    return provideFireEngine();
  }

  public static AppModule_ProvideFireEngineFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FireEngine provideFireEngine() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFireEngine());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideFireEngineFactory INSTANCE = new AppModule_ProvideFireEngineFactory();
  }
}
