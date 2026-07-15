package com.expenseai.domain.usecase;

import com.expenseai.ai.GemmaService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CategorizeExpenseUseCase_Factory implements Factory<CategorizeExpenseUseCase> {
  private final Provider<GemmaService> gemmaServiceProvider;

  public CategorizeExpenseUseCase_Factory(Provider<GemmaService> gemmaServiceProvider) {
    this.gemmaServiceProvider = gemmaServiceProvider;
  }

  @Override
  public CategorizeExpenseUseCase get() {
    return newInstance(gemmaServiceProvider.get());
  }

  public static CategorizeExpenseUseCase_Factory create(
      Provider<GemmaService> gemmaServiceProvider) {
    return new CategorizeExpenseUseCase_Factory(gemmaServiceProvider);
  }

  public static CategorizeExpenseUseCase newInstance(GemmaService gemmaService) {
    return new CategorizeExpenseUseCase(gemmaService);
  }
}
