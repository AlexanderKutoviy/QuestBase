package com.questbase.app.usage.controller;

import com.questbase.app.time.TimeProvider;
import com.questbase.app.usage.AppUsage;
import com.questbase.app.usage.dao.UsageDao;
import com.questbase.app.usage.packageprovider.AppPackageProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

public class DefaultUsageController implements UsageController {

    private static final long SCAN_PERIOD = 1;

    private UsageDao usageDao;
    private AppPackageProvider appPackageProvider;
    private TimeProvider timeProvider;
    private Subscription scanSubscription;

    public DefaultUsageController(AppPackageProvider appPackageProvider, TimeProvider timeProvider, UsageDao usageDao) {
        this.usageDao = usageDao;
        this.appPackageProvider = appPackageProvider;
        this.timeProvider = timeProvider;
    }

    @Override
    public List<AppUsage> getAppUsages() {
        return usageDao.getAppUsages();
    }

    @Override
    public void stopScanning() {
        if (scanSubscription != null && !scanSubscription.isUnsubscribed()) {
            scanSubscription.unsubscribe();
        }
    }

    @Override
    public void startScanning() {
        if (scanSubscription == null || scanSubscription.isUnsubscribed()) {
            scanSubscription = Observable.interval(0, SCAN_PERIOD, TimeUnit.SECONDS)
                    .onBackpressureLatest()
                    .subscribe(time -> logVisiblePackage());
        }
    }

    private void logVisiblePackage() {
        appPackageProvider.getVisibleAppPackage()
                .map(packageName -> new AppUsage(packageName, timeProvider.getCurrentTimeMillis()))
                .ifPresent(usageDao::replaceLatest);
    }
}