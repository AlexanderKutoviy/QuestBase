package com.questbase.app.permission;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;

public class AfterLollipopPermissionManager implements PermissionManager {

    private final String packageName;
    private final AppOpsManager appOpsManager;

    public AfterLollipopPermissionManager(AppOpsManager appOpsManager, String packageName) {
        this.appOpsManager = appOpsManager;
        this.packageName = packageName;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean hasUsageStatsPermission() {
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), packageName);
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
