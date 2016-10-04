package com.questbase.app.controller.personalresult.weeklyusercountcontroller;

import com.annimon.stream.Stream;
import com.questbase.app.controller.Event;
import com.questbase.app.dao.personalresult.weeklyusercount.WeeklyUserCountDao;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCountContainer;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class DefaultWeeklyUserCountController implements WeeklyUserCountController {

    private final WeeklyUserCountDao weeklyUserCountDao;

    private PublishSubject<Event<WeeklyUserCount>> publishSubject;

    public DefaultWeeklyUserCountController(WeeklyUserCountDao weeklyUserCountDao) {
        this.weeklyUserCountDao = weeklyUserCountDao;
        this.publishSubject = PublishSubject.create();
    }

    @Override
    public Observable<Event<WeeklyUserCount>> observe() {
        return publishSubject;
    }

    @Override
    public List<WeeklyUserCount> getAllData() {
        return weeklyUserCountDao.read();
    }

    @Override
    public void setChartPoints(WeeklyUserCountContainer weeklyUserCountContainer) {
        List<String> cnt = countChartPoints(weeklyUserCountContainer.cnt, weeklyUserCountContainer.before);
        Stream.range(0, weeklyUserCountContainer.days.size() - 1)
                .forEach(i -> {
                    WeeklyUserCount weeklyUserCount = new WeeklyUserCount(weeklyUserCountContainer.days.get(i), cnt.get(i));
                    weeklyUserCountDao.create(weeklyUserCount);
                    publishSubject.onNext(new Event<>(weeklyUserCount, Event.Type.WRITE));
                });
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