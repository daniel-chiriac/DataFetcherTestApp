package com.chiriacd.datafetch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.chiriacd.datafetch.persistence.DataStore;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ServerDetailActivity extends AppCompatActivity {

    @Inject DataStore dataStore;

    private EditText serverPortInput;
    private EditText serverAddressInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_server_detail);
        Button updateButton = findViewById(R.id.update_server_button);
        serverPortInput = findViewById(R.id.server_port);
        serverAddressInput = findViewById(R.id.server_address);

        serverPortInput.setText(String.valueOf(dataStore.getPort()));
        serverAddressInput.setText(dataStore.getServerAddress());

        updateButton.setOnClickListener(v -> updateAndFinish());
    }

    private void updateAndFinish() {
        updateServerDetails();
        finish();
    }

    private void updateServerDetails() {
        String port = serverPortInput.getText().toString();
        String address = serverAddressInput.getText().toString();
        if (!port.isEmpty()) {
            dataStore.savePort(Integer.valueOf(port));
        }
        if (!address.isEmpty()) {
            dataStore.saveServerAddress(address);
        }
    }

}
