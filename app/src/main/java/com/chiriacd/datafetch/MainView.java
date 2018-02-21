package com.chiriacd.datafetch;

import com.chiriacd.datafetch.model.Response;

public interface MainView {
    void updateResponseView(Response response);

    void navigateToServerDetailActivity();

    void updateServerDetails(String serverAddress, int port);
}
