package com.example.liyangos3323.hanselpermisson.permission;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.liyangos3323.hanselpermisson.MApp;

/**
 * Created by liyangos3323 on 2017/6/5.
 */

public class SPSetting {

    private static SPSetting spSetting;

    public static SPSetting getInstance() {
        if (spSetting == null) {
            spSetting = new SPSetting();
        }
        return spSetting;
    }

    private SPSetting() {

    }

    public SharedPreferences getSP(Context context) {
        if (context == null) {
            context = MApp.mApp;
        }
        return SPManager.getInstance().getSP(context, "mSP");
    }

    public void setAuthGranted(Context cxt, boolean isGranted) {
        getSP(cxt).edit().putBoolean("authGranted", isGranted).commit();
    }

    public boolean getAuthGranted(Context cxt) {
        return getSP(cxt).getBoolean("authGranted", false);
    }

    public void setAllPermissionGranted(Context cxt, boolean isAllGranted) {
        getSP(cxt).edit().putBoolean("allPermissionGranted", isAllGranted).commit();
    }

    public boolean getAllPermissionGranted(Context cx) {
        return getSP(cx).getBoolean("allPermissionGranted", false);
    }
}
