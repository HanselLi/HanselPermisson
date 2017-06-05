package com.example.liyangos3323.hanselpermisson;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.app.AlertDialog;

/**
 * Created by liyangos3323 on 2017/6/5.
 */

public class AlertDialogEx extends AlertDialog {

    protected AlertDialogEx(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected AlertDialogEx(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
