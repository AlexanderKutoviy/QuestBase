package com.questbase.app.usage.packageprovider;

import android.app.ActivityManager;
import android.content.ComponentName;

import com.annimon.stream.Optional;

import java.util.List;

/**
 * Provides top application package for pre-lollipop android versions
 */
public class PreLollipopAppPackageProvider implements AppPackageProvider {

    private final ActivityManager activityManager;

    public PreLollipopAppPackageProvider(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Optional<String> getVisibleAppPackage() {
        String topPackageName = null;
        List<ActivityManager.RunningTaskInfo> runningTask = activityManager.getRunningTasks(1);
        if (runningTask != null) {
            ActivityManager.RunningTaskInfo taskTop = runningTask.get(0);
            ComponentName componentTop = taskTop.topActivity;
            topPackageName = componentTop.getPackageName();
        }
        return Optional.ofNullable(topPackageName);
    }
}
