package com.questbase.app.controller.analytics;

public interface AnalyticsController {

    void logSyncDurationEvent(long durationTime, long lastSyncTime);

    void logFilesCopyingDurationEvent(long durationTime);
}