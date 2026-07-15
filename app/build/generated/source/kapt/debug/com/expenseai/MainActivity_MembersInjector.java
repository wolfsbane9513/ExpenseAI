package com.expenseai;

import com.expenseai.security.BiometricHelper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<BiometricHelper> biometricHelperProvider;

  public MainActivity_MembersInjector(Provider<BiometricHelper> biometricHelperProvider) {
    this.biometricHelperProvider = biometricHelperProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<BiometricHelper> biometricHelperProvider) {
    return new MainActivity_MembersInjector(biometricHelperProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectBiometricHelper(instance, biometricHelperProvider.get());
  }

  @InjectedFieldSignature("com.expenseai.MainActivity.biometricHelper")
  public static void injectBiometricHelper(MainActivity instance, BiometricHelper biometricHelper) {
    instance.biometricHelper = biometricHelper;
  }
}
