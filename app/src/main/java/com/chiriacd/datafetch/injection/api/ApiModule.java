package com.chiriacd.datafetch.injection.api;

import android.content.SharedPreferences;

import com.chiriacd.datafetch.network.RootFetchInterceptor;
import com.chiriacd.datafetch.api.RootFetchService;
import com.chiriacd.datafetch.api.NextPageService;
import com.chiriacd.datafetch.persistence.DataStore;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    private static final String BASIC = "basic";
    private static final String INTERCEPTED = "intercepted";


    @Provides
    @Named(INTERCEPTED)
    public Retrofit provideInterceptedRetrofit(@Named(INTERCEPTED) OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl("http://localhost")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    @Provides
    @Named(BASIC)
    public Retrofit provideRetrofit(@Named(BASIC) OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl("http://localhost")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    @Provides
    @Named(INTERCEPTED)
    OkHttpClient httpClient(Interceptor interceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        return httpClient.build();
    }

    @Provides
    @Named(BASIC)
    OkHttpClient basicHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    Interceptor interceptor(DataStore dataStore) {
        return new RootFetchInterceptor(dataStore);
    }

    @Provides
    public RootFetchService provideDataFetchService(@Named(INTERCEPTED) Retrofit retrofit) {
        return retrofit.create(RootFetchService.class);
    }

    @Provides
    public NextPageService provideNextPageService(@Named(BASIC) Retrofit retrofit) {
        return retrofit.create(NextPageService.class);
    }
}
