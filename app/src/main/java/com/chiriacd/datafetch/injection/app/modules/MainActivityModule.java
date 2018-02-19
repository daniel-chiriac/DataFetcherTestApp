package com.chiriacd.datafetch.injection.app.modules;

import android.app.Activity;

import com.chiriacd.datafetch.MainActivity;
import com.chiriacd.datafetch.MainView;
import com.chiriacd.datafetch.injection.app.MainActivitySubcomponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = MainActivitySubcomponent.class)
public abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourActivityInjectorFactory(MainActivitySubcomponent.Builder builder);

    @Binds
    public abstract MainView bindMainView(MainActivity mainActivity);
}

