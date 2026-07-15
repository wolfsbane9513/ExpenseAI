package com.expenseai.di;

import android.content.Context;
import com.expenseai.data.local.ExpenseDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideExpenseDatabaseFactory implements Factory<ExpenseDatabase> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideExpenseDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ExpenseDatabase get() {
    return provideExpenseDatabase(contextProvider.get());
  }

  public static AppModule_ProvideExpenseDatabaseFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideExpenseDatabaseFactory(contextProvider);
  }

  public static ExpenseDatabase provideExpenseDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExpenseDatabase(context));
  }
}
