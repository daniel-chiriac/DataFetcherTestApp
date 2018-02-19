package com.chiriacd.datafetch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ServerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_detail);
        findViewById(R.id.update_server_button);
    }
}
