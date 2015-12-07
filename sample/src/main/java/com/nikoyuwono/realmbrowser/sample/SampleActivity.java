package com.nikoyuwono.realmbrowser.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nikoyuwono.realmbrowser.RealmBrowser;

public class SampleActivity extends AppCompatActivity {

    private RealmBrowser realmBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    @Override
    protected void onResume() {
        super.onResume();
        realmBrowser = new RealmBrowser();
        realmBrowser.start();
        realmBrowser.showServerAddress(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (realmBrowser != null) {
            realmBrowser.stop();
        }
    }
}
