package com.questbase.app.controller.personalresult.weeklyusercountcontroller;

import com.questbase.app.controller.Event;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCountContainer;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCount;

import java.util.List;

import rx.Observable;

public interface WeeklyUserCountController {

    Observable<Event<WeeklyUserCount>> observe();

    List<WeeklyUserCount> getAllData();

    void setChartPoints(WeeklyUserCountContainer weeklyUserCountContainer);
}