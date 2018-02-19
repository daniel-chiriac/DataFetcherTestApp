package com.chiriacd.datafetch.persistence;


import android.content.SharedPreferences;

import com.chiriacd.datafetch.model.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import javax.inject.Inject;

public class DataStore {

    private static final String DATA_KEY = "DataStore.data";

    private SharedPreferences preferences;

    @Inject public DataStore(SharedPreferences preferences) {
        this.preferences = preferences;
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
}
