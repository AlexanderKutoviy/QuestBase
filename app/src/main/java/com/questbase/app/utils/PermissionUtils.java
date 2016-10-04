package com.questbase.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class PermissionUtils {

    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    public static boolean checkWriteExternalStoragePermission(Context context) {
        String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                WRITE_EXTERNAL_STORAGE);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestWriteExternalStoragePermission(Activity activity) {
        requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }
}