package com.questbase.app.usage.screen;

import com.questbase.app.usage.AppUsage;

import java.util.List;

public interface UsageView {

    void setHasPermission(boolean hasPermission);

    void displayAppUsages(List<AppUsage> appUsages);
}
