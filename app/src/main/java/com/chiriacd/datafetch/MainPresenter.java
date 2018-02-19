package com.chiriacd.datafetch;


import com.chiriacd.datafetch.api.NextPageService;
import com.chiriacd.datafetch.api.NextPath;
import com.chiriacd.datafetch.api.RootFetchService;
import com.chiriacd.datafetch.model.CodesManager;
import com.chiriacd.datafetch.model.Response;
import com.chiriacd.datafetch.persistence.DataStore;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {

    @Inject RootFetchService rootFetchService;
    @Inject NextPageService nextPageService;

    private DataStore dataStore;
    private CodesManager codesManager;

    private MainView mainView;
    private CompositeDisposable disposable;

    private Response lastResponse;

    @Inject
    public MainPresenter(DataStore dataStore) {
        this.dataStore = dataStore;
        codesManager = new CodesManager(dataStore.getCodeMap());
        disposable = new CompositeDisposable();
    }

    public void refreshData() {
        disposable.add(rootFetchService.getRoot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(rootPage -> nextPageService.getNextPath(rootPage.getNextPath()))
                .subscribe(nextPathObservable -> nextPathObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(NextPath::getResponseCode)
                        .doOnNext(codesManager::add)
                        .map(codesManager.getData()::get)
                        .doOnNext(response -> lastResponse = response)
                        .subscribe(mainView::updateResponseView)));
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response response) {
        lastResponse = response;
    }

    public void saveData() {
        dataStore.saveCodeMap(codesManager.getData());
    }

    public void attachView(MainView mainView) {
        this.mainView = mainView;
    }

    public void detach() {
        disposable.clear();
        mainView = null;
    }
}
