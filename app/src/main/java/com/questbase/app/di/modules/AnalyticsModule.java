package com.questbase.app.di.modules;

import android.content.Context;

import com.questbase.app.controller.analytics.AnalyticsController;
import com.questbase.app.controller.analytics.FirebaseAnalyticsController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {
    @Singleton
    @Provides
    AnalyticsController provideAnaluticsControllerController(Context context) {
        return new FirebaseAnalyticsController(context);
    }
}