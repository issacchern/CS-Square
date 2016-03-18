package com.chernyee.cssquare;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Issac on 3/17/2016.
 */
public class MyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}