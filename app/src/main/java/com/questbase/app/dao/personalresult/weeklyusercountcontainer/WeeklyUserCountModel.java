package com.questbase.app.dao.personalresult.weeklyusercountcontainer;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;

@Table(database = RespoDatabase.class)
public class WeeklyUserCountModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public String day;

    @Column
    public String cnt;

    public WeeklyUserCountModel() {
    }

    public WeeklyUserCountModel(WeeklyUserCount weeks) {
        day = weeks.day;
        cnt = weeks.cnt;
    }

    public WeeklyUserCount toWeek() {
        return new WeeklyUserCount(day, cnt);
    }
}