package com.expenseai.ui.screens.scan;

import com.expenseai.ai.GemmaService;
import com.expenseai.ai.OCRService;
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
public final class ScanViewModel_Factory implements Factory<ScanViewModel> {
  private final Provider<OCRService> ocrServiceProvider;

  private final Provider<GemmaService> gemmaServiceProvider;

  private final Provider<ExpenseRepository> repositoryProvider;

  public ScanViewModel_Factory(Provider<OCRService> ocrServiceProvider,
      Provider<GemmaService> gemmaServiceProvider, Provider<ExpenseRepository> repositoryProvider) {
    this.ocrServiceProvider = ocrServiceProvider;
    this.gemmaServiceProvider = gemmaServiceProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ScanViewModel get() {
    return newInstance(ocrServiceProvider.get(), gemmaServiceProvider.get(), repositoryProvider.get());
  }

  public static ScanViewModel_Factory create(Provider<OCRService> ocrServiceProvider,
      Provider<GemmaService> gemmaServiceProvider, Provider<ExpenseRepository> repositoryProvider) {
    return new ScanViewModel_Factory(ocrServiceProvider, gemmaServiceProvider, repositoryProvider);
  }

  public static ScanViewModel newInstance(OCRService ocrService, GemmaService gemmaService,
      ExpenseRepository repository) {
    return new ScanViewModel(ocrService, gemmaService, repository);
  }
}
