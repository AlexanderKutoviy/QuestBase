package com.questbase.app.usage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.questbase.app.QuestBaseApplication;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerService;
import com.questbase.app.usage.controller.UsageController;

import javax.inject.Inject;

public class UsageService extends Service {

    public static final String START_SCANNING = "start_scanning";
    public static final String STOP_SCANNING = "stop_scanning";

    @Inject
    UsageController usageController;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerUsageService_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        if (intent.getBooleanExtra(START_SCANNING, false)) {
            usageController.startScanning();
            return START_REDELIVER_INTENT;
        } else if (intent.getBooleanExtra(STOP_SCANNING, false)) {
            usageController.stopScanning();
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerService
    interface Component {
        void inject(UsageService usageRegistrationService);
    }
}
