package com.questbase.app.di.modules;

import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import com.questbase.app.permission.AfterLollipopPermissionManager;
import com.questbase.app.permission.PermissionManager;
import com.questbase.app.permission.PreLollipopPermissionManager;
import com.questbase.app.time.SystemTimeProvider;
import com.questbase.app.time.TimeProvider;
import com.questbase.app.utils.PrefsHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {

    private Application application;

    public AndroidModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    TimeProvider provideTimeProvider() {
        return new SystemTimeProvider();
    }

    @Singleton
    @Provides
    PermissionManager providePermissionManager(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            AppOpsManager appOpsManager =
                    (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            return new AfterLollipopPermissionManager(appOpsManager, context.getPackageName());
        } else {
            return new PreLollipopPermissionManager();
        }
    }

    @Singleton
    @Provides
    PrefsHelper providePrefsHelper(Context context) {
        return new PrefsHelper(context);
    }

    @Singleton
    @Provides
    AssetManager provideAssetManager() {
        return application.getAssets();
    }
}
