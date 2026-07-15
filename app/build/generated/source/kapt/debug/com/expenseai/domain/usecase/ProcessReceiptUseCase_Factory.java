package com.expenseai.domain.usecase;

import com.expenseai.ai.GemmaService;
import com.expenseai.ai.OCRService;
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
public final class ProcessReceiptUseCase_Factory implements Factory<ProcessReceiptUseCase> {
  private final Provider<OCRService> ocrServiceProvider;

  private final Provider<GemmaService> gemmaServiceProvider;

  public ProcessReceiptUseCase_Factory(Provider<OCRService> ocrServiceProvider,
      Provider<GemmaService> gemmaServiceProvider) {
    this.ocrServiceProvider = ocrServiceProvider;
    this.gemmaServiceProvider = gemmaServiceProvider;
  }

  @Override
  public ProcessReceiptUseCase get() {
    return newInstance(ocrServiceProvider.get(), gemmaServiceProvider.get());
  }

  public static ProcessReceiptUseCase_Factory create(Provider<OCRService> ocrServiceProvider,
      Provider<GemmaService> gemmaServiceProvider) {
    return new ProcessReceiptUseCase_Factory(ocrServiceProvider, gemmaServiceProvider);
  }

  public static ProcessReceiptUseCase newInstance(OCRService ocrService,
      GemmaService gemmaService) {
    return new ProcessReceiptUseCase(ocrService, gemmaService);
  }
}
