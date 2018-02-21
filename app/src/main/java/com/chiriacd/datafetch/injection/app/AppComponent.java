package com.chiriacd.datafetch.injection.app;


import com.chiriacd.datafetch.injection.api.ApiModule;
import com.chiriacd.datafetch.injection.app.modules.AppContextModule;
import com.chiriacd.datafetch.injection.app.modules.MainActivityModule;
import com.chiriacd.datafetch.injection.app.modules.ServerDetailActivityModule;

import dagger.Component;


@Component(modules = {MainActivityModule.class, ApiModule.class,
        AppContextModule.class,
        ServerDetailActivityModule.class})
@AppScope
public interface AppComponent {
    void inject(MyApp application);
}
