package com.expenseai;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.expenseai.ai.EmailParser;
import com.expenseai.ai.GemmaModelManager;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.OCRService;
import com.expenseai.ai.SmsParser;
import com.expenseai.data.local.ExpenseDao;
import com.expenseai.data.local.ExpenseDatabase;
import com.expenseai.data.local.FireModelDao;
import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.data.repository.FireRepository;
import com.expenseai.di.AppModule_ProvideExpenseDaoFactory;
import com.expenseai.di.AppModule_ProvideExpenseDatabaseFactory;
import com.expenseai.di.AppModule_ProvideFireEngineFactory;
import com.expenseai.di.AppModule_ProvideFireModelDaoFactory;
import com.expenseai.di.AppModule_ProvidePendingExpenseDaoFactory;
import com.expenseai.domain.fire.FireEngine;
import com.expenseai.domain.usecase.ProcessSharedTextUseCase;
import com.expenseai.security.BiometricHelper;
import com.expenseai.security.EncryptedPreferences;
import com.expenseai.ui.screens.dashboard.DashboardViewModel;
import com.expenseai.ui.screens.dashboard.DashboardViewModel_HiltModules_KeyModule_ProvideFactory;
import com.expenseai.ui.screens.firemodel.FireModelViewModel;
import com.expenseai.ui.screens.firemodel.FireModelViewModel_HiltModules_KeyModule_ProvideFactory;
import com.expenseai.ui.screens.history.HistoryViewModel;
import com.expenseai.ui.screens.history.HistoryViewModel_HiltModules_KeyModule_ProvideFactory;
import com.expenseai.ui.screens.insights.InsightsViewModel;
import com.expenseai.ui.screens.insights.InsightsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.expenseai.ui.screens.review.ReviewViewModel;
import com.expenseai.ui.screens.review.ReviewViewModel_HiltModules_KeyModule_ProvideFactory;
import com.expenseai.ui.screens.scan.ScanViewModel;
import com.expenseai.ui.screens.scan.ScanViewModel_HiltModules_KeyModule_ProvideFactory;
import com.expenseai.ui.screens.sources.SourcesViewModel;
import com.expenseai.ui.screens.sources.SourcesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerExpenseApp_HiltComponents_SingletonC {
  private DaggerExpenseApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ExpenseApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ExpenseApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ExpenseApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ExpenseApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ExpenseApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ExpenseApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ExpenseApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ExpenseApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ExpenseApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ExpenseApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ExpenseApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ExpenseApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ExpenseApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ExpenseApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ExpenseApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ExpenseApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ExpenseApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ExpenseApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ExpenseApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public void injectShareActivity(ShareActivity shareActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return ImmutableSet.<String>of(DashboardViewModel_HiltModules_KeyModule_ProvideFactory.provide(), FireModelViewModel_HiltModules_KeyModule_ProvideFactory.provide(), HistoryViewModel_HiltModules_KeyModule_ProvideFactory.provide(), InsightsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ReviewViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ScanViewModel_HiltModules_KeyModule_ProvideFactory.provide(), SourcesViewModel_HiltModules_KeyModule_ProvideFactory.provide());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectBiometricHelper(instance, singletonCImpl.biometricHelperProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends ExpenseApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<FireModelViewModel> fireModelViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<InsightsViewModel> insightsViewModelProvider;

    private Provider<ReviewViewModel> reviewViewModelProvider;

    private Provider<ScanViewModel> scanViewModelProvider;

    private Provider<SourcesViewModel> sourcesViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private ProcessSharedTextUseCase processSharedTextUseCase() {
      return new ProcessSharedTextUseCase(singletonCImpl.emailParserProvider.get(), singletonCImpl.smsParserProvider.get(), singletonCImpl.gemmaServiceProvider.get(), singletonCImpl.providePendingExpenseDaoProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.fireModelViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.insightsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.reviewViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.scanViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.sourcesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(7).put("com.expenseai.ui.screens.dashboard.DashboardViewModel", ((Provider) dashboardViewModelProvider)).put("com.expenseai.ui.screens.firemodel.FireModelViewModel", ((Provider) fireModelViewModelProvider)).put("com.expenseai.ui.screens.history.HistoryViewModel", ((Provider) historyViewModelProvider)).put("com.expenseai.ui.screens.insights.InsightsViewModel", ((Provider) insightsViewModelProvider)).put("com.expenseai.ui.screens.review.ReviewViewModel", ((Provider) reviewViewModelProvider)).put("com.expenseai.ui.screens.scan.ScanViewModel", ((Provider) scanViewModelProvider)).put("com.expenseai.ui.screens.sources.SourcesViewModel", ((Provider) sourcesViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<String, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.expenseai.ui.screens.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.expenseRepositoryProvider.get(), singletonCImpl.gemmaServiceProvider.get(), singletonCImpl.gemmaModelManagerProvider.get(), singletonCImpl.fireRepositoryProvider.get(), singletonCImpl.provideFireEngineProvider.get());

          case 1: // com.expenseai.ui.screens.firemodel.FireModelViewModel 
          return (T) new FireModelViewModel(singletonCImpl.fireRepositoryProvider.get());

          case 2: // com.expenseai.ui.screens.history.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.expenseRepositoryProvider.get(), singletonCImpl.fireRepositoryProvider.get(), singletonCImpl.provideFireEngineProvider.get());

          case 3: // com.expenseai.ui.screens.insights.InsightsViewModel 
          return (T) new InsightsViewModel(singletonCImpl.expenseRepositoryProvider.get(), singletonCImpl.gemmaServiceProvider.get());

          case 4: // com.expenseai.ui.screens.review.ReviewViewModel 
          return (T) new ReviewViewModel(singletonCImpl.providePendingExpenseDaoProvider.get(), singletonCImpl.expenseRepositoryProvider.get(), viewModelCImpl.processSharedTextUseCase());

          case 5: // com.expenseai.ui.screens.scan.ScanViewModel 
          return (T) new ScanViewModel(singletonCImpl.oCRServiceProvider.get(), singletonCImpl.gemmaServiceProvider.get(), singletonCImpl.expenseRepositoryProvider.get());

          case 6: // com.expenseai.ui.screens.sources.SourcesViewModel 
          return (T) new SourcesViewModel(singletonCImpl.providePendingExpenseDaoProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ExpenseApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ExpenseApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends ExpenseApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<EncryptedPreferences> encryptedPreferencesProvider;

    private Provider<BiometricHelper> biometricHelperProvider;

    private Provider<ExpenseDatabase> provideExpenseDatabaseProvider;

    private Provider<ExpenseDao> provideExpenseDaoProvider;

    private Provider<ExpenseRepository> expenseRepositoryProvider;

    private Provider<GemmaModelManager> gemmaModelManagerProvider;

    private Provider<GemmaService> gemmaServiceProvider;

    private Provider<FireModelDao> provideFireModelDaoProvider;

    private Provider<FireRepository> fireRepositoryProvider;

    private Provider<FireEngine> provideFireEngineProvider;

    private Provider<PendingExpenseDao> providePendingExpenseDaoProvider;

    private Provider<EmailParser> emailParserProvider;

    private Provider<SmsParser> smsParserProvider;

    private Provider<OCRService> oCRServiceProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.encryptedPreferencesProvider = DoubleCheck.provider(new SwitchingProvider<EncryptedPreferences>(singletonCImpl, 1));
      this.biometricHelperProvider = DoubleCheck.provider(new SwitchingProvider<BiometricHelper>(singletonCImpl, 0));
      this.provideExpenseDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<ExpenseDatabase>(singletonCImpl, 4));
      this.provideExpenseDaoProvider = DoubleCheck.provider(new SwitchingProvider<ExpenseDao>(singletonCImpl, 3));
      this.expenseRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ExpenseRepository>(singletonCImpl, 2));
      this.gemmaModelManagerProvider = DoubleCheck.provider(new SwitchingProvider<GemmaModelManager>(singletonCImpl, 6));
      this.gemmaServiceProvider = DoubleCheck.provider(new SwitchingProvider<GemmaService>(singletonCImpl, 5));
      this.provideFireModelDaoProvider = DoubleCheck.provider(new SwitchingProvider<FireModelDao>(singletonCImpl, 8));
      this.fireRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FireRepository>(singletonCImpl, 7));
      this.provideFireEngineProvider = DoubleCheck.provider(new SwitchingProvider<FireEngine>(singletonCImpl, 9));
      this.providePendingExpenseDaoProvider = DoubleCheck.provider(new SwitchingProvider<PendingExpenseDao>(singletonCImpl, 10));
      this.emailParserProvider = DoubleCheck.provider(new SwitchingProvider<EmailParser>(singletonCImpl, 11));
      this.smsParserProvider = DoubleCheck.provider(new SwitchingProvider<SmsParser>(singletonCImpl, 12));
      this.oCRServiceProvider = DoubleCheck.provider(new SwitchingProvider<OCRService>(singletonCImpl, 13));
    }

    @Override
    public void injectExpenseApp(ExpenseApp expenseApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.expenseai.security.BiometricHelper 
          return (T) new BiometricHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.encryptedPreferencesProvider.get());

          case 1: // com.expenseai.security.EncryptedPreferences 
          return (T) new EncryptedPreferences(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.expenseai.data.repository.ExpenseRepository 
          return (T) new ExpenseRepository(singletonCImpl.provideExpenseDaoProvider.get());

          case 3: // com.expenseai.data.local.ExpenseDao 
          return (T) AppModule_ProvideExpenseDaoFactory.provideExpenseDao(singletonCImpl.provideExpenseDatabaseProvider.get());

          case 4: // com.expenseai.data.local.ExpenseDatabase 
          return (T) AppModule_ProvideExpenseDatabaseFactory.provideExpenseDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.expenseai.ai.GemmaService 
          return (T) new GemmaService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.gemmaModelManagerProvider.get());

          case 6: // com.expenseai.ai.GemmaModelManager 
          return (T) new GemmaModelManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.expenseai.data.repository.FireRepository 
          return (T) new FireRepository(singletonCImpl.provideFireModelDaoProvider.get());

          case 8: // com.expenseai.data.local.FireModelDao 
          return (T) AppModule_ProvideFireModelDaoFactory.provideFireModelDao(singletonCImpl.provideExpenseDatabaseProvider.get());

          case 9: // com.expenseai.domain.fire.FireEngine 
          return (T) AppModule_ProvideFireEngineFactory.provideFireEngine();

          case 10: // com.expenseai.data.local.PendingExpenseDao 
          return (T) AppModule_ProvidePendingExpenseDaoFactory.providePendingExpenseDao(singletonCImpl.provideExpenseDatabaseProvider.get());

          case 11: // com.expenseai.ai.EmailParser 
          return (T) new EmailParser();

          case 12: // com.expenseai.ai.SmsParser 
          return (T) new SmsParser();

          case 13: // com.expenseai.ai.OCRService 
          return (T) new OCRService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
