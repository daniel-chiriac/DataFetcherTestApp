package com.chiriacd.datafetch.model;


import android.support.annotation.NonNull;

import java.util.HashMap;

public class CodesManager {

    private HashMap<String, Response> data;

    public CodesManager(@NonNull HashMap<String, Response> data) {
        this.data = data;
    }

    public void add(String responseCode) {
        Response response = new Response(responseCode);
        if (data.containsKey(responseCode)) {
            response = data.get(responseCode);
        }
        response.increment();
        data.put(responseCode,response);
    }

    public HashMap<String, Response> getData() {
        return data;
    }
}
