package com.expenseai.ui.screens.dashboard;

import com.expenseai.ai.GemmaModelManager;
import com.expenseai.ai.GemmaService;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<ExpenseRepository> repositoryProvider;

  private final Provider<GemmaService> gemmaServiceProvider;

  private final Provider<GemmaModelManager> modelManagerProvider;

  private final Provider<FireRepository> fireRepositoryProvider;

  private final Provider<FireEngine> fireEngineProvider;

  public DashboardViewModel_Factory(Provider<ExpenseRepository> repositoryProvider,
      Provider<GemmaService> gemmaServiceProvider, Provider<GemmaModelManager> modelManagerProvider,
      Provider<FireRepository> fireRepositoryProvider, Provider<FireEngine> fireEngineProvider) {
    this.repositoryProvider = repositoryProvider;
    this.gemmaServiceProvider = gemmaServiceProvider;
    this.modelManagerProvider = modelManagerProvider;
    this.fireRepositoryProvider = fireRepositoryProvider;
    this.fireEngineProvider = fireEngineProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(repositoryProvider.get(), gemmaServiceProvider.get(), modelManagerProvider.get(), fireRepositoryProvider.get(), fireEngineProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<ExpenseRepository> repositoryProvider,
      Provider<GemmaService> gemmaServiceProvider, Provider<GemmaModelManager> modelManagerProvider,
      Provider<FireRepository> fireRepositoryProvider, Provider<FireEngine> fireEngineProvider) {
    return new DashboardViewModel_Factory(repositoryProvider, gemmaServiceProvider, modelManagerProvider, fireRepositoryProvider, fireEngineProvider);
  }

  public static DashboardViewModel newInstance(ExpenseRepository repository,
      GemmaService gemmaService, GemmaModelManager modelManager, FireRepository fireRepository,
      FireEngine fireEngine) {
    return new DashboardViewModel(repository, gemmaService, modelManager, fireRepository, fireEngine);
  }
}
