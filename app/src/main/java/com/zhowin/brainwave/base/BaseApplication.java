package com.zhowin.brainwave.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static BaseApplication getInstance() {
        return instance;
    }
}
