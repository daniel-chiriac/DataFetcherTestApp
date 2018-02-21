package com.chiriacd.datafetch.network;

import com.chiriacd.datafetch.persistence.DataStore;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RootFetchInterceptor implements Interceptor {

    private String server;
    private int port;

    public RootFetchInterceptor(DataStore dataStore) {
        updateServerDetails(dataStore);
        dataStore.dataChangeObservable().subscribe(this::updateServerDetails);
    }

    private void updateServerDetails(DataStore dataStore) {
        server = dataStore.getServerAddress();
        port = dataStore.getPort();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(server)
                .port(port)
                .build();

        Request request = original.newBuilder()
                .url(url)
                .build();

        return chain.proceed(request);
    }
}
