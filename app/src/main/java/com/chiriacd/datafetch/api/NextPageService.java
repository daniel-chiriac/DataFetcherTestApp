package com.chiriacd.datafetch.api;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NextPageService {

    @GET
    Observable<NextPath> getNextPath(@Url String nextPath);
}
