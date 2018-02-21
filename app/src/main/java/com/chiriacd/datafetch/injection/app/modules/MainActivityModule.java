package com.chiriacd.datafetch.injection.app.modules;

import android.app.Activity;

import com.chiriacd.datafetch.MainActivity;
import com.chiriacd.datafetch.MainView;
import com.chiriacd.datafetch.ServerDetailActivity;
import com.chiriacd.datafetch.injection.app.MainActivitySubcomponent;
import com.chiriacd.datafetch.injection.app.ServerDetailActivitySubcomponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {MainActivitySubcomponent.class, ServerDetailActivitySubcomponent.class})
public abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(ServerDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindServerDetailActivityInjectorFactory(ServerDetailActivitySubcomponent.Builder builder);

    @Binds
    public abstract MainView bindMainView(MainActivity mainActivity);
}

