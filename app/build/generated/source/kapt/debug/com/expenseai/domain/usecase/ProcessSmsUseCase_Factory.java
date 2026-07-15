package com.expenseai.domain.usecase;

import com.expenseai.ai.GemmaService;
import com.expenseai.ai.SmsParser;
import com.expenseai.ai.SmsReader;
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
public final class ProcessSmsUseCase_Factory implements Factory<ProcessSmsUseCase> {
  private final Provider<SmsReader> smsReaderProvider;

  private final Provider<SmsParser> smsParserProvider;

  private final Provider<GemmaService> gemmaServiceProvider;

  private final Provider<PendingExpenseDao> pendingDaoProvider;

  public ProcessSmsUseCase_Factory(Provider<SmsReader> smsReaderProvider,
      Provider<SmsParser> smsParserProvider, Provider<GemmaService> gemmaServiceProvider,
      Provider<PendingExpenseDao> pendingDaoProvider) {
    this.smsReaderProvider = smsReaderProvider;
    this.smsParserProvider = smsParserProvider;
    this.gemmaServiceProvider = gemmaServiceProvider;
    this.pendingDaoProvider = pendingDaoProvider;
  }

  @Override
  public ProcessSmsUseCase get() {
    return newInstance(smsReaderProvider.get(), smsParserProvider.get(), gemmaServiceProvider.get(), pendingDaoProvider.get());
  }

  public static ProcessSmsUseCase_Factory create(Provider<SmsReader> smsReaderProvider,
      Provider<SmsParser> smsParserProvider, Provider<GemmaService> gemmaServiceProvider,
      Provider<PendingExpenseDao> pendingDaoProvider) {
    return new ProcessSmsUseCase_Factory(smsReaderProvider, smsParserProvider, gemmaServiceProvider, pendingDaoProvider);
  }

  public static ProcessSmsUseCase newInstance(SmsReader smsReader, SmsParser smsParser,
      GemmaService gemmaService, PendingExpenseDao pendingDao) {
    return new ProcessSmsUseCase(smsReader, smsParser, gemmaService, pendingDao);
  }
}
