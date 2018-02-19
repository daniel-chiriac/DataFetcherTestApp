package com.chiriacd.datafetch.network;

import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RootFetchInterceptor implements Interceptor {

    private final static String BASE_URL_KEY = "RootFetchInterceptor.BaseURL";
    private final static String PORT_KEY = "RootFetchInterceptor.Port";

    private String baseUrl;
    private int port;

    public RootFetchInterceptor(SharedPreferences preferences) {
        setBaseUrl(preferences);
        setPort(preferences);
        registerObservers(preferences);

    }

    private void registerObservers(SharedPreferences preferences) {
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if (key.equals(BASE_URL_KEY)) {
                setBaseUrl(sharedPreferences);
            } else if (key.equals(PORT_KEY)) {
                setPort(sharedPreferences);
            }
        });
    }

    private void setBaseUrl(SharedPreferences preferences) {
        baseUrl = preferences.getString(BASE_URL_KEY, "http://localhost");
    }

    private void setPort(SharedPreferences preferences) {
        port = preferences.getInt(PORT_KEY, 8000);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(baseUrl)
                .port(port)
                .build();

        Request request = original.newBuilder()
                .url(url)
                .build();

        return chain.proceed(request);
    }
}
