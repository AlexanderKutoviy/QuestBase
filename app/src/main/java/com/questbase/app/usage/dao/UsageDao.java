package com.questbase.app.usage.dao;

import com.questbase.app.usage.AppUsage;

import java.util.List;

public interface UsageDao {

    List<AppUsage> getAppUsages();

    /**
     * Updates latest app usage record if package is the same.
     * Creates new app usage record if package is different
     * @param appUsage new app usage record to append
     */
    void replaceLatest(AppUsage appUsage);
}
