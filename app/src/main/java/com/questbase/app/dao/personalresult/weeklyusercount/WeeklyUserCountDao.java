package com.questbase.app.dao.personalresult.weeklyusercount;

import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCount;

import java.util.List;

public interface WeeklyUserCountDao {

    void create(WeeklyUserCount weeklyUserCount);

    List<WeeklyUserCount> read();

    void update(WeeklyUserCount weeklyUserCount);

    void delete(WeeklyUserCount weeklyUserCount);
}