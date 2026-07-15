package com.expenseai.di;

import com.expenseai.data.local.ExpenseDatabase;
import com.expenseai.data.local.PendingExpenseDao;
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
public final class AppModule_ProvidePendingExpenseDaoFactory implements Factory<PendingExpenseDao> {
  private final Provider<ExpenseDatabase> databaseProvider;

  public AppModule_ProvidePendingExpenseDaoFactory(Provider<ExpenseDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PendingExpenseDao get() {
    return providePendingExpenseDao(databaseProvider.get());
  }

  public static AppModule_ProvidePendingExpenseDaoFactory create(
      Provider<ExpenseDatabase> databaseProvider) {
    return new AppModule_ProvidePendingExpenseDaoFactory(databaseProvider);
  }

  public static PendingExpenseDao providePendingExpenseDao(ExpenseDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePendingExpenseDao(database));
  }
}
