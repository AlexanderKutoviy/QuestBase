package com.questbase.app;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.DaggerAppComponent;
import com.questbase.app.di.modules.AndroidModule;
import com.questbase.app.utils.AssetsUtils;

public class QuestBaseApplication extends Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .androidModule(new AndroidModule(this)).build();
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    public synchronized static AppComponent getAppComponent() {
        return appComponent;
    }

    public static void migrateDataBase() {
        AssetsUtils.copyDatabase(getAppComponent().getContext(), appComponent.getPrefsHelper());
    }
}