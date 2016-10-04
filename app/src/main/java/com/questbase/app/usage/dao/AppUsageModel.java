package com.questbase.app.usage.dao;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.usage.AppUsage;

@Table(database = RespoDatabase.class)
public class AppUsageModel extends RespoBaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    long id;

    @Column
    String packageName;

    @Column
    long utcTime;

    public AppUsageModel() {
    }

    public AppUsageModel(long id, String packageName, long utcTime) {
        this.id = id;
        this.packageName = packageName;
        this.utcTime = utcTime;
    }

    public AppUsageModel(AppUsage appUsage) {
        this.packageName = appUsage.packageName;
        this.utcTime = appUsage.utcTime;
    }

    public AppUsage toAppUsage() {
        return new AppUsage(packageName, utcTime);
    }
}
