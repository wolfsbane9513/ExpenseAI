package com.expenseai.ai;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class SmsParser_Factory implements Factory<SmsParser> {
  @Override
  public SmsParser get() {
    return newInstance();
  }

  public static SmsParser_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SmsParser newInstance() {
    return new SmsParser();
  }

  private static final class InstanceHolder {
    private static final SmsParser_Factory INSTANCE = new SmsParser_Factory();
  }
}
