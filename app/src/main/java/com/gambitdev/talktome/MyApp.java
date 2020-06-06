package com.gambitdev.talktome;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApp extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
