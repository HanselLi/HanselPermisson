package com.example.liyangos3323.hanselpermisson;

import android.app.Application;

/**
 * Created by liyangos3323 on 2017/6/5.
 */

public class MApp extends Application {
    public static MApp mApp ;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
    public MApp getApp(){
        return mApp;
    }
}
