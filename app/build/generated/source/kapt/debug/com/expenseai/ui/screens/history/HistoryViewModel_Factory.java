package com.expenseai.ui.screens.history;

import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.data.repository.FireRepository;
import com.expenseai.domain.fire.FireEngine;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<ExpenseRepository> repositoryProvider;

  private final Provider<FireRepository> fireRepositoryProvider;

  private final Provider<FireEngine> fireEngineProvider;

  public HistoryViewModel_Factory(Provider<ExpenseRepository> repositoryProvider,
      Provider<FireRepository> fireRepositoryProvider, Provider<FireEngine> fireEngineProvider) {
    this.repositoryProvider = repositoryProvider;
    this.fireRepositoryProvider = fireRepositoryProvider;
    this.fireEngineProvider = fireEngineProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(repositoryProvider.get(), fireRepositoryProvider.get(), fireEngineProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<ExpenseRepository> repositoryProvider,
      Provider<FireRepository> fireRepositoryProvider, Provider<FireEngine> fireEngineProvider) {
    return new HistoryViewModel_Factory(repositoryProvider, fireRepositoryProvider, fireEngineProvider);
  }

  public static HistoryViewModel newInstance(ExpenseRepository repository,
      FireRepository fireRepository, FireEngine fireEngine) {
    return new HistoryViewModel(repository, fireRepository, fireEngine);
  }
}
