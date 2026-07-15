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
public final class EmailParser_Factory implements Factory<EmailParser> {
  @Override
  public EmailParser get() {
    return newInstance();
  }

  public static EmailParser_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EmailParser newInstance() {
    return new EmailParser();
  }

  private static final class InstanceHolder {
    private static final EmailParser_Factory INSTANCE = new EmailParser_Factory();
  }
}
