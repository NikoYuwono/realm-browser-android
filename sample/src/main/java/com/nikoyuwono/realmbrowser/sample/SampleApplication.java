package com.nikoyuwono.realmbrowser.sample;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by nyuwono on 12/7/15.
 */
public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("sample.realm")
                .build();
        Realm.setDefaultConfiguration(config);
    }

}