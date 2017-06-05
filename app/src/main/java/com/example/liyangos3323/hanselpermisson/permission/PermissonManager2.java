package com.example.liyangos3323.hanselpermisson.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.example.liyangos3323.hanselpermisson.MApp;

/**
 * Created by liyangos3323 on 2017/6/5.
 */

public class PermissonManager2 {
    private static PermissonManager2 permissonManager2;

    public static PermissonManager2 getInstance() {
        if (permissonManager2 == null) {
            permissonManager2 = new PermissonManager2();
        }
        return permissonManager2;
    }

    private PermissonManager2() {

    }
    public boolean isGrantedPermission(String permission){
        if (checkPermisson(MApp.mApp,permission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }
    public int checkPermisson(Context context, String permisson) {
        if (context == null || TextUtils.isEmpty(permisson)) {
            return PackageManager.PERMISSION_DENIED;
        }
        if (Build.VERSION.SDK_INT < 23) {
            return PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(context, permisson);
        }
    }
}
