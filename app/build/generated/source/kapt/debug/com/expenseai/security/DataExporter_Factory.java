package com.expenseai.security;

import android.content.Context;
import com.expenseai.data.local.ExpenseDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DataExporter_Factory implements Factory<DataExporter> {
  private final Provider<Context> contextProvider;

  private final Provider<ExpenseDao> expenseDaoProvider;

  public DataExporter_Factory(Provider<Context> contextProvider,
      Provider<ExpenseDao> expenseDaoProvider) {
    this.contextProvider = contextProvider;
    this.expenseDaoProvider = expenseDaoProvider;
  }

  @Override
  public DataExporter get() {
    return newInstance(contextProvider.get(), expenseDaoProvider.get());
  }

  public static DataExporter_Factory create(Provider<Context> contextProvider,
      Provider<ExpenseDao> expenseDaoProvider) {
    return new DataExporter_Factory(contextProvider, expenseDaoProvider);
  }

  public static DataExporter newInstance(Context context, ExpenseDao expenseDao) {
    return new DataExporter(context, expenseDao);
  }
}
