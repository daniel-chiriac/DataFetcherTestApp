package com.chiriacd.datafetch.persistence;


import android.content.SharedPreferences;
import android.util.Log;

import com.chiriacd.datafetch.injection.app.AppScope;
import com.chiriacd.datafetch.model.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.chiriacd.datafetch.Config.DEFAULT_PORT;
import static com.chiriacd.datafetch.Config.DEFAULT_SERVER_ADDRESS;

@AppScope
public class DataStore {

    private static final String DATA_KEY = "DataStore.data";
    private static final String SERVER_ADDRESS_KEY = "DataStore.server";
    private static final String PORT_KEY = "DataStore.port";
    private static final String FIRST_RUN_KEY = "DataStore.isFirstRun";

    private SharedPreferences preferences;

    private PublishSubject<DataStore> subject = PublishSubject.create();

    @Inject public DataStore(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    private void notifyDataChanged() {
        subject.onNext(this);
    }

    public HashMap<String, Response> getCodeMap() {
        Gson gson = new Gson();
        String json = preferences.getString(DATA_KEY, "");
        Type type = new TypeToken<HashMap<String, Response>>() {}.getType();
        HashMap<String, Response> map = gson.fromJson(json, type);
        return map != null ? map : new HashMap<>();
    }

    public void saveCodeMap(HashMap<String, Response> map) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(map);
        editor.putString(DATA_KEY, json);
        editor.apply();
    }

    public void saveServerAddress(String address) {
        preferences.edit()
                .putString(SERVER_ADDRESS_KEY, address)
                .apply();
        notifyDataChanged();
    }

    public String getServerAddress() {
        return preferences.getString(SERVER_ADDRESS_KEY, DEFAULT_SERVER_ADDRESS);
    }

    public void savePort(int port) {
        preferences.edit()
                .putInt(PORT_KEY, port)
                .apply();
        notifyDataChanged();
    }

    public int getPort() {
        return preferences.getInt(PORT_KEY, DEFAULT_PORT);
    }

    public boolean isFirstRun() {
        return preferences.getBoolean(FIRST_RUN_KEY, true);
    }

    public void setFirstRun(boolean firstRun) {
        preferences.edit()
                .putBoolean(FIRST_RUN_KEY, firstRun)
                .apply();
    }

    public Observable<DataStore> dataChangeObservable() {
        return subject;
    }
}
