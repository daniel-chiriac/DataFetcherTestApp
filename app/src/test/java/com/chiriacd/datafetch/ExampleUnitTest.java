package com.chiriacd.datafetch;

import com.chiriacd.datafetch.api.NextPageService;
import com.chiriacd.datafetch.api.NextPath;
import com.chiriacd.datafetch.api.RootFetchService;
import com.chiriacd.datafetch.api.RootPage;
import com.chiriacd.datafetch.model.Response;
import com.chiriacd.datafetch.persistence.DataStore;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Mock DataStore dataStore;
    @Mock NextPath nextPath;
    @Mock RootPage rootPage;
    @Mock RootFetchService rootFetchService;
    @Mock NextPageService nextPageService;
    @Mock MainView mainView;

    MainPresenter presenter;

    @BeforeClass
    public static void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Scheduler.Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @Before public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenter(dataStore, rootFetchService, nextPageService);
    }
    @Test
    public void examplePresenterTest() throws Exception {
        //todo CodeManager should be mocked as well
        when(rootFetchService.getRoot()).thenReturn(Observable.just(rootPage));
        when(rootPage.getNextPath()).thenReturn("123");
        when(nextPageService.getNextPath(anyString())).thenReturn(Observable.just(nextPath));
        when(nextPath.getResponseCode()).thenReturn("123");
        when(nextPath.getPath()).thenReturn("123");

        when(dataStore.dataChangeObservable()).thenReturn(Observable.just(dataStore));
        when(dataStore.getServerAddress()).thenReturn("123");
        when(dataStore.getPort()).thenReturn(123);

        presenter.attachView(mainView);
        presenter.refreshData();
        verify(mainView).updateResponseView(any());
    }
}