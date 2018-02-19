package com.chiriacd.datafetch.injection.app;


import com.chiriacd.datafetch.injection.api.ApiModule;
import com.chiriacd.datafetch.injection.app.modules.AppContextModule;
import com.chiriacd.datafetch.injection.app.modules.MainActivityModule;

import dagger.Component;


@Component(modules = {MainActivityModule.class, ApiModule.class,
        AppContextModule.class})
@AppScope
public interface AppComponent {
    void inject(MyApp application);
}
