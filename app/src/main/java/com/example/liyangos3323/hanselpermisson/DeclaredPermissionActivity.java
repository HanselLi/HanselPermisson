package com.example.liyangos3323.hanselpermisson;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.liyangos3323.hanselpermisson.permission.PermissonManager2;
import com.example.liyangos3323.hanselpermisson.permission.SPSetting;

import java.util.ArrayList;
import java.util.List;

public class DeclaredPermissionActivity extends AppCompatActivity {

    private static Boolean sIsMiOS = null;

    /**
     * 主要逻辑：先弹出一个dialog让用户同意，之后检查权限。
     * 核心是shouldShowRequestPermissionRationale，见方法注释
     *
     * @onCreate 需要注意检查用户是否设置中关闭了权限
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declared_permisson);
        Log.e("tag", "user close ? " + PermissonManager2.getInstance().isPermissionDenied());
        if (!SPSetting.getInstance().getAllPermissionGranted(this)) {
            showAuthAndCheckPermission();
        } else if (PermissonManager2.getInstance().isPermissionDenied()) {
            checkMPermission();
        }
    }

    private void showAuthAndCheckPermission() {
        if (SPSetting.getInstance().getAuthGranted(this)) {
            checkMPermission();
            return;
        }
        new AlertDialog.Builder(this).setTitle("请求权限了").setCancelable(true)
                .setIcon(R.mipmap.ic_launcher).setMessage("不给权限你就等着崩溃吧")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPSetting.getInstance().setAuthGranted(DeclaredPermissionActivity.this, true);
                        checkMPermission();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SPSetting.getInstance().setAuthGranted(DeclaredPermissionActivity.this, false);
                exitApp();
            }
        }).create().show();
    }

    private void exitApp() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Process.killProcess(Process.myPid());
            }
        }, 200);
    }

    List<String> mPermissionArray = new ArrayList();

    private void checkMPermission() {
        addRequestPermission(Manifest.permission.CAMERA);
        addRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(this, mPermissionArray.toArray(new String[mPermissionArray.size()]), 100);
    }

    private void addRequestPermission(String permission) {//make a statement to distinguish whether the permission have been acquired
        if (PermissonManager2.getInstance().isGrantedPermission(permission)) {
            return;
        }
        mPermissionArray.add(permission);
    }

    public static boolean isMIOS() {
        if (sIsMiOS != null) {
            return sIsMiOS;
        }

        sIsMiOS = false;
        String mi = "MI";
        if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.startsWith(mi)) {
            sIsMiOS = true;
            return true;
        }

        PackageManager packageManager = MApp.mApp
                .getPackageManager();
        try {
            PackageInfo pkgInfo = packageManager.getPackageInfo(
                    "com.miui.cloudservice", 0);
            sIsMiOS = (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            sIsMiOS = false;
        }

        return sIsMiOS;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isExitApp = false;     // 是否退出浏览器
        boolean isGoSettings = false;   // 是否去系统设置APP权限界面
        if (requestCode == 100) {
            if (isMIOS() && mPermissionArray.size() > permissions.length) {
                permissions = mPermissionArray.toArray(new String[mPermissionArray.size()]);
            }// deal with MI permission
            //小米特殊，如果申请的权限5个，可能返回2个，所以判断一下，少于申请的个数话就重赋值
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean b = showRationalUI(permissions[i]);
                    Log.e("tag", "shouldShow -->" + b);
                    if (!b) {
                        isGoSettings = true;
                    }
                    isExitApp = true;
                }
            }
            if (isGoSettings) {
                goToAppSetting();
            } else if (!isExitApp) {
                SPSetting.getInstance().setAllPermissionGranted(this, true);
                startActivity(new Intent(DeclaredPermissionActivity.this, MainActivity.class));
                finish();
            } else {
                SPSetting.getInstance().setAllPermissionGranted(this, false);
                exitApp();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 关于shouldShowRequestPermissionRationale函数的注意事项：
     * 1).应用安装后第一次访问，则直接返回false；
     * 2).第一次请求权限时，用户Deny了，再次调用shouldShowRequestPermissionRationale()，则返回true；
     * 3).第二次请求权限时，用户Deny了，并选择了“dont ask me again”的选项时，再次调用shouldShowRequestPermissionRationale()时，返回false；
     * 4).设备的系统设置中，禁止了应用获取这个权限的授权，则调用shouldShowRequestPermissionRationale()，返回false。
     */
    private boolean showRationalUI(String permission) {
        boolean b = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        return b;
    }

    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            checkMPermission();
        }
    }
}
