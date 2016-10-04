package com.questbase.app.dao.personalresult.weeklyusercount;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCount;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCountModel;

import java.util.List;

public class DbFlowWeeklyUserCountDao implements WeeklyUserCountDao {

    @Override
    public void create(WeeklyUserCount weeklyUserCount) {
        new WeeklyUserCountModel(weeklyUserCount).saveOnDuplicateUpdate();
    }

    @Override
    public List<WeeklyUserCount> read() {
        return Stream.of(SQLite.select()
                .from(WeeklyUserCountModel.class).queryList())
                .map(WeeklyUserCountModel::toWeek)
                .collect(Collectors.toList());
    }

    @Override
    public void update(WeeklyUserCount weeklyUserCount) {
        create(weeklyUserCount);
    }

    @Override
    public void delete(WeeklyUserCount weeklyUserCount) {
        new WeeklyUserCountModel(weeklyUserCount).delete();
    }
}