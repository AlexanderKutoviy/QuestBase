package com.questbase.app.usage.packageprovider;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

import com.annimon.stream.Optional;
import com.questbase.app.time.TimeProvider;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Application package provider for versions including and after lollipop
 */
public class AfterLollipopPackageProvider implements AppPackageProvider {

    private static final long SCAN_TIME_GAP = 10_000;

    private final UsageStatsManager usageStatsManager;
    private final TimeProvider timeProvider;

    public AfterLollipopPackageProvider(TimeProvider timeProvider, UsageStatsManager usageStatsManager) {
        this.usageStatsManager = usageStatsManager;
        this.timeProvider = timeProvider;
    }

    @SuppressLint("NewApi")
    @Override
    public Optional<String> getVisibleAppPackage() {
        String topPackageName = null;
        long time = timeProvider.getCurrentTimeMillis();
        List<UsageStats> stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, time - SCAN_TIME_GAP, time);
        if (stats != null) {
            SortedMap<Long, UsageStats> runningTask = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                runningTask.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (runningTask.isEmpty()) {
                return Optional.empty();
            }
            topPackageName = runningTask.get(runningTask.lastKey()).getPackageName();
        }
        return Optional.ofNullable(topPackageName);
    }
}
