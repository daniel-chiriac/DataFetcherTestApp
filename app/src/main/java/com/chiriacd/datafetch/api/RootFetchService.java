package com.chiriacd.datafetch.api;


import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RootFetchService {

    @GET("/")
    Observable<RootPage> getRoot();
}
