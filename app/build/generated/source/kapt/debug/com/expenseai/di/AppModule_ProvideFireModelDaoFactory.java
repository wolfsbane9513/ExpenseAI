package com.expenseai.di;

import com.expenseai.data.local.ExpenseDatabase;
import com.expenseai.data.local.FireModelDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideFireModelDaoFactory implements Factory<FireModelDao> {
  private final Provider<ExpenseDatabase> databaseProvider;

  public AppModule_ProvideFireModelDaoFactory(Provider<ExpenseDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public FireModelDao get() {
    return provideFireModelDao(databaseProvider.get());
  }

  public static AppModule_ProvideFireModelDaoFactory create(
      Provider<ExpenseDatabase> databaseProvider) {
    return new AppModule_ProvideFireModelDaoFactory(databaseProvider);
  }

  public static FireModelDao provideFireModelDao(ExpenseDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFireModelDao(database));
  }
}
