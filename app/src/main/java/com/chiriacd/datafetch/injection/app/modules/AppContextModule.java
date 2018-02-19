package com.chiriacd.datafetch.injection.app.modules;

import android.content.Context;
import android.content.SharedPreferences;


import com.chiriacd.datafetch.injection.app.AppScope;

import dagger.Module;
import dagger.Provides;


@Module
public class AppContextModule {

    private Context context;

    public AppContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @AppScope
    Context provideContext() {
        return context;
    }

    @Provides
    @AppScope
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences("default", Context.MODE_PRIVATE);
    }
}
