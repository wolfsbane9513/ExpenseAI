package com.expenseai.domain.usecase;

import com.expenseai.ai.EmailParser;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.SmsParser;
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
public final class ProcessSharedTextUseCase_Factory implements Factory<ProcessSharedTextUseCase> {
  private final Provider<EmailParser> emailParserProvider;

  private final Provider<SmsParser> smsParserProvider;

  private final Provider<GemmaService> gemmaServiceProvider;

  private final Provider<PendingExpenseDao> pendingDaoProvider;

  public ProcessSharedTextUseCase_Factory(Provider<EmailParser> emailParserProvider,
      Provider<SmsParser> smsParserProvider, Provider<GemmaService> gemmaServiceProvider,
      Provider<PendingExpenseDao> pendingDaoProvider) {
    this.emailParserProvider = emailParserProvider;
    this.smsParserProvider = smsParserProvider;
    this.gemmaServiceProvider = gemmaServiceProvider;
    this.pendingDaoProvider = pendingDaoProvider;
  }

  @Override
  public ProcessSharedTextUseCase get() {
    return newInstance(emailParserProvider.get(), smsParserProvider.get(), gemmaServiceProvider.get(), pendingDaoProvider.get());
  }

  public static ProcessSharedTextUseCase_Factory create(Provider<EmailParser> emailParserProvider,
      Provider<SmsParser> smsParserProvider, Provider<GemmaService> gemmaServiceProvider,
      Provider<PendingExpenseDao> pendingDaoProvider) {
    return new ProcessSharedTextUseCase_Factory(emailParserProvider, smsParserProvider, gemmaServiceProvider, pendingDaoProvider);
  }

  public static ProcessSharedTextUseCase newInstance(EmailParser emailParser, SmsParser smsParser,
      GemmaService gemmaService, PendingExpenseDao pendingDao) {
    return new ProcessSharedTextUseCase(emailParser, smsParser, gemmaService, pendingDao);
  }
}
