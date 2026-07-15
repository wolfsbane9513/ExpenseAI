package com.expenseai.data.repository;

import com.expenseai.data.local.FireModelDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class FireRepository_Factory implements Factory<FireRepository> {
  private final Provider<FireModelDao> fireModelDaoProvider;

  public FireRepository_Factory(Provider<FireModelDao> fireModelDaoProvider) {
    this.fireModelDaoProvider = fireModelDaoProvider;
  }

  @Override
  public FireRepository get() {
    return newInstance(fireModelDaoProvider.get());
  }

  public static FireRepository_Factory create(Provider<FireModelDao> fireModelDaoProvider) {
    return new FireRepository_Factory(fireModelDaoProvider);
  }

  public static FireRepository newInstance(FireModelDao fireModelDao) {
    return new FireRepository(fireModelDao);
  }
}
