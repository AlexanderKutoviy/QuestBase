package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.personalresult.weeklyusercount.DbFlowWeeklyUserCountDao;
import com.questbase.app.dao.personalresult.weeklyusercount.WeeklyUserCountDao;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowWeeklyUserCountDaoTests {

    private final int AMOUNT = 10;

    @Test
    public void createReadWeeklyUserCount() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        WeeklyUserCountDao weeklyUserCountDao = new DbFlowWeeklyUserCountDao();

        for (WeeklyUserCount weeklyUserCount : weeklyUserCountList()) {
            weeklyUserCountDao.create(weeklyUserCount);
        }

        assertEquals(weeklyUserCountList(), weeklyUserCountDao.read());

        FlowManager.destroy();
    }

    private List<WeeklyUserCount> weeklyUserCountList() {
        List<WeeklyUserCount> weeklyUserCounts = new ArrayList<>(AMOUNT);
        for (int i = 0; i < AMOUNT; i++) {
            weeklyUserCounts.add(new WeeklyUserCount(String.valueOf(i), String.valueOf(i)));
        }
        return weeklyUserCounts;
    }
}