package com.chiriacd.datafetch.injection.app;

import com.chiriacd.datafetch.ServerDetailActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface ServerDetailActivitySubcomponent extends AndroidInjector<ServerDetailActivity> {

    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<ServerDetailActivity> {

    }
}