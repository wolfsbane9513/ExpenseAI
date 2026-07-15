package com.expenseai.ui.screens.review;

import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.domain.usecase.ProcessSharedTextUseCase;
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
public final class ReviewViewModel_Factory implements Factory<ReviewViewModel> {
  private final Provider<PendingExpenseDao> pendingDaoProvider;

  private final Provider<ExpenseRepository> repositoryProvider;

  private final Provider<ProcessSharedTextUseCase> processSharedTextUseCaseProvider;

  public ReviewViewModel_Factory(Provider<PendingExpenseDao> pendingDaoProvider,
      Provider<ExpenseRepository> repositoryProvider,
      Provider<ProcessSharedTextUseCase> processSharedTextUseCaseProvider) {
    this.pendingDaoProvider = pendingDaoProvider;
    this.repositoryProvider = repositoryProvider;
    this.processSharedTextUseCaseProvider = processSharedTextUseCaseProvider;
  }

  @Override
  public ReviewViewModel get() {
    return newInstance(pendingDaoProvider.get(), repositoryProvider.get(), processSharedTextUseCaseProvider.get());
  }

  public static ReviewViewModel_Factory create(Provider<PendingExpenseDao> pendingDaoProvider,
      Provider<ExpenseRepository> repositoryProvider,
      Provider<ProcessSharedTextUseCase> processSharedTextUseCaseProvider) {
    return new ReviewViewModel_Factory(pendingDaoProvider, repositoryProvider, processSharedTextUseCaseProvider);
  }

  public static ReviewViewModel newInstance(PendingExpenseDao pendingDao,
      ExpenseRepository repository, ProcessSharedTextUseCase processSharedTextUseCase) {
    return new ReviewViewModel(pendingDao, repository, processSharedTextUseCase);
  }
}
