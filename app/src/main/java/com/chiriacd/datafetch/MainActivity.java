package com.chiriacd.datafetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.chiriacd.datafetch.model.Response;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements MainView {

    private TextView responseCodeView;
    private TextView timesFetchedView;
    private TextView serverDetailsView;

    @Inject MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initUI();
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.persistData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //todo should move this magic string
        outState.putParcelable("lastResponse", presenter.getLastResponse());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //todo should move this magic string
        Response savedResponse = savedInstanceState.getParcelable("lastResponse");
        presenter.setLastResponse(savedResponse);
        updateResponseView(savedResponse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            navigateToServerDetailActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    public void updateResponseView(Response response) {
        if (response == null) return;
        responseCodeView.setText(response.getCode());
        timesFetchedView.setText(String.format(getString(R.string.times_fetched), response.getTimesFetched()));
    }

    @Override
    public void navigateToServerDetailActivity() {
        startActivity(new Intent(this, ServerDetailActivity.class));
    }

    @Override
    public void updateServerDetails(String serverAddress, int port) {
        serverDetailsView.setText(String.format(getString(R.string.server_details), serverAddress, port));
    }

    @Override
    public void showSomethingWentWrong(String localizedMessage) {
        //todo probably shouldn't reuse variables intended for other purposes
        responseCodeView.setText(String.format(getString(R.string.something_went_wrong), localizedMessage));
        timesFetchedView.setText("");
    }

    @Override
    public void showTimeout() {
        responseCodeView.setText(R.string.timeout);
        timesFetchedView.setText("");
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        timesFetchedView = findViewById(R.id.times_fetched);
        responseCodeView = findViewById(R.id.response_code);
        serverDetailsView = findViewById(R.id.server_details);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> presenter.refreshData());
    }
}
