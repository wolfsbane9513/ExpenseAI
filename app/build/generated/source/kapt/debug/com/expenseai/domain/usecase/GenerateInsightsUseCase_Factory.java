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
public final class GenerateInsightsUseCase_Factory implements Factory<GenerateInsightsUseCase> {
  private final Provider<GemmaService> gemmaServiceProvider;

  public GenerateInsightsUseCase_Factory(Provider<GemmaService> gemmaServiceProvider) {
    this.gemmaServiceProvider = gemmaServiceProvider;
  }

  @Override
  public GenerateInsightsUseCase get() {
    return newInstance(gemmaServiceProvider.get());
  }

  public static GenerateInsightsUseCase_Factory create(
      Provider<GemmaService> gemmaServiceProvider) {
    return new GenerateInsightsUseCase_Factory(gemmaServiceProvider);
  }

  public static GenerateInsightsUseCase newInstance(GemmaService gemmaService) {
    return new GenerateInsightsUseCase(gemmaService);
  }
}
