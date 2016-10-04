package com.questbase.app.usage.controller;

import com.questbase.app.usage.AppUsage;

import java.util.List;

public interface UsageController {

    List<AppUsage> getAppUsages();

    void startScanning();

    void stopScanning();
}