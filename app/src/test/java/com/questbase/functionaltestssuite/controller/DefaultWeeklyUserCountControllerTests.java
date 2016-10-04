package com.questbase.functionaltestssuite.controller;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.DefaultWeeklyUserCountController;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.WeeklyUserCountController;
import com.questbase.app.dao.personalresult.weeklyusercount.DbFlowWeeklyUserCountDao;
import com.questbase.app.dao.personalresult.weeklyusercount.WeeklyUserCountDao;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCount;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCountContainer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DefaultWeeklyUserCountControllerTests {

    private final int AMOUNT = 10;

    @Test
    public void setGetTests() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        WeeklyUserCountDao weeklyUserCountDao = new DbFlowWeeklyUserCountDao();
        WeeklyUserCountController weeklyUserCountController = new DefaultWeeklyUserCountController(weeklyUserCountDao);

        weeklyUserCountController.setChartPoints(initWeeklyUserCountContainer());

        assertEquals(generateResult(), weeklyUserCountController.getAllData());

        FlowManager.destroy();
    }

    private WeeklyUserCountContainer initWeeklyUserCountContainer() {
        String before = String.valueOf(AMOUNT);
        List<String> days = new ArrayList<>(AMOUNT);
        List<String> count = new ArrayList<>(AMOUNT);
        for (int i = 0; i < AMOUNT; i++) {
            days.add(String.valueOf(i));
            count.add(String.valueOf(i));
        }
        return new WeeklyUserCountContainer(days, count, before);
    }

    private List<WeeklyUserCount> generateResult() {
        WeeklyUserCountContainer weeklyUserCountContainer = initWeeklyUserCountContainer();
        List<String> cnt = countChartPoints(weeklyUserCountContainer.cnt, weeklyUserCountContainer.before);
        List<WeeklyUserCount> result = new ArrayList<>();
        for (int i = 0; i < weeklyUserCountContainer.days.size() - 1; i++) {
            result.add(new WeeklyUserCount(weeklyUserCountContainer.days.get(i), cnt.get(i)));
        }
        return result;
    }

    private List<String> countChartPoints(List<String> points, String before) {
        if (points.isEmpty()) {
            return Collections.emptyList();
        }
        Long preCountingAmount = Long.parseLong(before);
        long previousPoint;
        long currentPoint;
        List<String> result = new ArrayList<>(points.size());

        previousPoint = Long.parseLong(points.get(0)) + preCountingAmount;
        for (int i = 1; i < points.size(); i++) {
            currentPoint = Long.parseLong(points.get(i)) + previousPoint;
            result.add(String.valueOf(currentPoint));
            previousPoint = currentPoint;
        }
        return result;
    }
}