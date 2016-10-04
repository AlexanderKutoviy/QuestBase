package com.questbase.app.controller.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.TimeUnit;

public class FirebaseAnalyticsController implements AnalyticsController {

    public final String TAG = FirebaseAnalyticsController.class.getSimpleName();
    private final FirebaseAnalytics firebaseAnalytics;
    private final String REPORT_NAME_TITLE = "report_name";
    private final String SYNC_REPORT_NAME = "sync_duration";
    private final String LAST_SYNC_REPORT_NAME = "time_since_last_sync";
    private final String SYNC_EVENT_TITLE = "sync_event";
    private final String FILES_COPYING_REPORT_NAME = "files_copying_duration";
    private final String FILES_COPYING_EVENT_TITLE = "files_copying_event";

    public FirebaseAnalyticsController(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void logSyncDurationEvent(long durationTime, long lastSyncTime) {
        long time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastSyncTime);
        Bundle params = new Bundle();
        params.putString(REPORT_NAME_TITLE, SYNC_REPORT_NAME);
        params.putString(SYNC_REPORT_NAME, String.valueOf(durationTime));
        params.putString(LAST_SYNC_REPORT_NAME, String.valueOf(time));
        firebaseAnalytics.logEvent(SYNC_EVENT_TITLE, params);
        firebaseAnalytics.setUserProperty(SYNC_REPORT_NAME, String.valueOf(durationTime));
        firebaseAnalytics.setUserProperty(LAST_SYNC_REPORT_NAME, String.valueOf(time));
    }

    @Override
    public void logFilesCopyingDurationEvent(long durationTime) {
        Bundle params = new Bundle();
        params.putString(REPORT_NAME_TITLE, FILES_COPYING_REPORT_NAME);
        params.putString(FILES_COPYING_REPORT_NAME, String.valueOf(durationTime));
        firebaseAnalytics.logEvent(FILES_COPYING_EVENT_TITLE, params);
        firebaseAnalytics.setUserProperty(FILES_COPYING_REPORT_NAME, String.valueOf(durationTime));
    }
}