package com.expenseai.ui.screens.insights;

import com.expenseai.ai.GemmaService;
import com.expenseai.data.repository.ExpenseRepository;
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
public final class InsightsViewModel_Factory implements Factory<InsightsViewModel> {
  private final Provider<ExpenseRepository> repositoryProvider;

  private final Provider<GemmaService> gemmaServiceProvider;

  public InsightsViewModel_Factory(Provider<ExpenseRepository> repositoryProvider,
      Provider<GemmaService> gemmaServiceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.gemmaServiceProvider = gemmaServiceProvider;
  }

  @Override
  public InsightsViewModel get() {
    return newInstance(repositoryProvider.get(), gemmaServiceProvider.get());
  }

  public static InsightsViewModel_Factory create(Provider<ExpenseRepository> repositoryProvider,
      Provider<GemmaService> gemmaServiceProvider) {
    return new InsightsViewModel_Factory(repositoryProvider, gemmaServiceProvider);
  }

  public static InsightsViewModel newInstance(ExpenseRepository repository,
      GemmaService gemmaService) {
    return new InsightsViewModel(repository, gemmaService);
  }
}
