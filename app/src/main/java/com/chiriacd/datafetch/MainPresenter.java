package com.chiriacd.datafetch;


import com.chiriacd.datafetch.api.NextPageService;
import com.chiriacd.datafetch.api.NextPath;
import com.chiriacd.datafetch.api.RootFetchService;
import com.chiriacd.datafetch.model.CodesManager;
import com.chiriacd.datafetch.model.Response;
import com.chiriacd.datafetch.persistence.DataStore;

import java.net.SocketTimeoutException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {

    RootFetchService rootFetchService;
    NextPageService nextPageService;

    private DataStore dataStore;
    private CodesManager codesManager;

    private MainView mainView;
    private CompositeDisposable compositeDisposable;

    private Response lastResponse;

    @Inject
    public MainPresenter(DataStore dataStore, RootFetchService rootFetchService, NextPageService nextPageService) {
        this.dataStore = dataStore;
        this.rootFetchService = rootFetchService;
        this.nextPageService = nextPageService;
        codesManager = new CodesManager(dataStore.getCodeMap());//todo perhaps should be moved from here
        compositeDisposable = new CompositeDisposable();

    }

    public void refreshData() {
        compositeDisposable.add(rootFetchService.getRoot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(rootPage -> nextPageService.getNextPath(rootPage.getNextPath()))
                .subscribe(nextPathObservable -> nextPathObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(NextPath::getResponseCode)
                        .doOnNext(codesManager::add)
                        .map(codesManager.getData()::get)
                        .doOnNext(response -> lastResponse = response)
                        .subscribe(mainView::updateResponseView, this::handleRootFetchErrors), this::handleRootFetchErrors));
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response response) {
        lastResponse = response;
    }

    public void persistData() {
        dataStore.saveCodeMap(codesManager.getData());
    }

    public void attachView(MainView mainView) {
        this.mainView = mainView;
        if (dataStore.isFirstRun()) {
            dataStore.setFirstRun(false);
            mainView.navigateToServerDetailActivity();
        } else {
            mainView.updateServerDetails(dataStore.getServerAddress(), dataStore.getPort());
        }
        compositeDisposable.add(dataStore
                .dataChangeObservable()
                .subscribe(dataStore1 -> mainView.updateServerDetails(dataStore1.getServerAddress(), dataStore1.getPort())));
    }

    private void handleRootFetchErrors(Throwable t) {
        if (t instanceof SocketTimeoutException) {
            mainView.showTimeout();
        } else {
            mainView.showSomethingWentWrong(t.getLocalizedMessage());
        }

    }

    public void detach() {
        compositeDisposable.clear();
        mainView = null;
    }
}
