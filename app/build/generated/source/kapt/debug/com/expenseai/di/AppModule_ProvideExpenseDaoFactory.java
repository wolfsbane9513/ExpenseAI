package com.expenseai.di;

import com.expenseai.data.local.ExpenseDao;
import com.expenseai.data.local.ExpenseDatabase;
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
public final class AppModule_ProvideExpenseDaoFactory implements Factory<ExpenseDao> {
  private final Provider<ExpenseDatabase> databaseProvider;

  public AppModule_ProvideExpenseDaoFactory(Provider<ExpenseDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ExpenseDao get() {
    return provideExpenseDao(databaseProvider.get());
  }

  public static AppModule_ProvideExpenseDaoFactory create(
      Provider<ExpenseDatabase> databaseProvider) {
    return new AppModule_ProvideExpenseDaoFactory(databaseProvider);
  }

  public static ExpenseDao provideExpenseDao(ExpenseDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExpenseDao(database));
  }
}
