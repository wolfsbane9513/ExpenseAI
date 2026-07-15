package com.expenseai.ui.screens.firemodel;

import com.expenseai.data.repository.FireRepository;
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
public final class FireModelViewModel_Factory implements Factory<FireModelViewModel> {
  private final Provider<FireRepository> repositoryProvider;

  public FireModelViewModel_Factory(Provider<FireRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public FireModelViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static FireModelViewModel_Factory create(Provider<FireRepository> repositoryProvider) {
    return new FireModelViewModel_Factory(repositoryProvider);
  }

  public static FireModelViewModel newInstance(FireRepository repository) {
    return new FireModelViewModel(repository);
  }
}
