package com.questbase.app.controller.personalresult.statisticscharts;

import com.questbase.app.controller.Syncable;
import com.questbase.app.domain.Form;
import com.questbase.app.net.entity.statistics.PersonalStatsDto;

import rx.Observable;

public interface StatisticsController {

    void setStats(PersonalStatsDto stats);

    Observable<PersonalStatsDto> getPersonalStats(long formId);

    Observable<Syncable.SyncResult> sync(Form form);
}