package com.expenseai.ui.screens.sources;

import com.expenseai.data.local.PendingExpenseDao;
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
public final class SourcesViewModel_Factory implements Factory<SourcesViewModel> {
  private final Provider<PendingExpenseDao> pendingDaoProvider;

  public SourcesViewModel_Factory(Provider<PendingExpenseDao> pendingDaoProvider) {
    this.pendingDaoProvider = pendingDaoProvider;
  }

  @Override
  public SourcesViewModel get() {
    return newInstance(pendingDaoProvider.get());
  }

  public static SourcesViewModel_Factory create(Provider<PendingExpenseDao> pendingDaoProvider) {
    return new SourcesViewModel_Factory(pendingDaoProvider);
  }

  public static SourcesViewModel newInstance(PendingExpenseDao pendingDao) {
    return new SourcesViewModel(pendingDao);
  }
}
