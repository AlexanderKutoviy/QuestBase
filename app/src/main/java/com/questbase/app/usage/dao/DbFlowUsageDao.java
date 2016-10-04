package com.questbase.app.usage.dao;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.usage.AppUsage;

import java.util.List;

public class DbFlowUsageDao implements UsageDao {

    @Override
    public List<AppUsage> getAppUsages() {
        return Stream.of(SQLite.select().from(AppUsageModel.class)
                .orderBy(AppUsageModel_Table.utcTime, false)
                .queryList())
                .map(AppUsageModel::toAppUsage)
                .collect(Collectors.toList());
    }

    @Override
    public void replaceLatest(AppUsage appUsage) {
        getLatestAppUsage()
                .filter(latestAppUsage -> latestAppUsage.packageName.equals(appUsage.packageName))
                .map(latestAppUsage -> new AppUsageModel(latestAppUsage.id, latestAppUsage.packageName, appUsage.utcTime))
                .orElse(new AppUsageModel(appUsage))
                .saveOnDuplicateUpdate();
    }

    private Optional<AppUsageModel> getLatestAppUsage() {
        return Optional.ofNullable(SQLite.select().from(AppUsageModel.class)
                .orderBy(AppUsageModel_Table.utcTime, false)
                .querySingle());
    }
}
