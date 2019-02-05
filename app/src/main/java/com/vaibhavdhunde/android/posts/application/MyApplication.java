package com.vaibhavdhunde.android.posts.application;

import android.app.Application;

import com.vaibhavdhunde.android.posts.BuildConfig;

import timber.log.Timber;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
