package com.shiva.imagepicker;

import android.app.Application;
import android.content.Context;

import com.shiva.imagepicker.di.DaggerMainComponent;
import com.shiva.imagepicker.di.MainComponent;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public class App extends Application {
    private static Context appContext;

    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        mainComponent = DaggerMainComponent.create();
    }

    public static Context getContext() {
        return appContext;
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }

}
