package com.example.liyangos3323.hanselpermisson.permission;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liyangos3323 on 2017/6/5.
 */

public class SPManager {
    private static SPManager spManager;

    public static SPManager getInstance() {
        if (spManager == null) {
            spManager = new SPManager();
        }
        return spManager;
    }

    private SPManager() {

    }

    public   SharedPreferences getSP(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


}
