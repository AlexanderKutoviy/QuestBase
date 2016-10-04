package com.questbase.app.di.modules;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import com.questbase.app.time.TimeProvider;
import com.questbase.app.usage.dao.DbFlowUsageDao;
import com.questbase.app.usage.controller.DefaultUsageController;
import com.questbase.app.usage.controller.UsageController;
import com.questbase.app.usage.dao.UsageDao;
import com.questbase.app.usage.packageprovider.AfterLollipopPackageProvider;
import com.questbase.app.usage.packageprovider.AppPackageProvider;
import com.questbase.app.usage.packageprovider.PreLollipopAppPackageProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UsageModule {

    @SuppressLint("NewApi")
    @Singleton
    @Provides
    UsageController provideUsageController(Context ctx, TimeProvider timeProvider, UsageDao usageDao) {
        AppPackageProvider appPackageProvider;
        if (Build.VERSION.SDK_INT >= 21) {
            appPackageProvider = new AfterLollipopPackageProvider(timeProvider,
                    (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE));
        } else {
            appPackageProvider = new PreLollipopAppPackageProvider(
                    (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE));
        }
        return new DefaultUsageController(appPackageProvider, timeProvider, usageDao);
    }

    @Singleton
    @Provides
    UsageDao provideUsageDao() {
        return new DbFlowUsageDao();
    }
}
