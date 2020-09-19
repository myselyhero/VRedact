package com.yongyong.redact;

import android.app.Application;

import com.yongyong.vredact.VRKit;

/**
 *
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VRKit.init(this);
    }
}
