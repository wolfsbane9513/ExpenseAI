package com.expenseai.domain.usecase;

import com.expenseai.ai.EmailParser;
import com.expenseai.ai.GemmaService;
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
public final class ProcessEmailUseCase_Factory implements Factory<ProcessEmailUseCase> {
  private final Provider<EmailParser> emailParserProvider;

  private final Provider<GemmaService> gemmaServiceProvider;

  private final Provider<PendingExpenseDao> pendingDaoProvider;

  public ProcessEmailUseCase_Factory(Provider<EmailParser> emailParserProvider,
      Provider<GemmaService> gemmaServiceProvider, Provider<PendingExpenseDao> pendingDaoProvider) {
    this.emailParserProvider = emailParserProvider;
    this.gemmaServiceProvider = gemmaServiceProvider;
    this.pendingDaoProvider = pendingDaoProvider;
  }

  @Override
  public ProcessEmailUseCase get() {
    return newInstance(emailParserProvider.get(), gemmaServiceProvider.get(), pendingDaoProvider.get());
  }

  public static ProcessEmailUseCase_Factory create(Provider<EmailParser> emailParserProvider,
      Provider<GemmaService> gemmaServiceProvider, Provider<PendingExpenseDao> pendingDaoProvider) {
    return new ProcessEmailUseCase_Factory(emailParserProvider, gemmaServiceProvider, pendingDaoProvider);
  }

  public static ProcessEmailUseCase newInstance(EmailParser emailParser, GemmaService gemmaService,
      PendingExpenseDao pendingDao) {
    return new ProcessEmailUseCase(emailParser, gemmaService, pendingDao);
  }
}
