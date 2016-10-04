package com.questbase.app.controller.personalresult.statisticscharts;

import android.content.Context;
import android.util.Log;

import com.questbase.app.CommonUtils;
import com.questbase.app.controller.Syncable;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.personalresult.personalstats.PerFormPersonalStatsDao;
import com.questbase.app.domain.Form;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.statistics.PersonalStatsDto;
import com.questbase.app.sync.SyncUtils;
import com.questbase.app.utils.RespoSchedulers;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class DefaultStatisticsController implements StatisticsController {

    private final RestApi restApi;
    private final Context context;
    private final AuthDao authDao;
    private final PerFormPersonalStatsDao perFormPersonalStatsDao;
    private final String POPULAR = "popular";
    private final String FILL_RATE = "fillRate";
    private final String FORMS_PER_USER = "formsPerUser";
    private ReplaySubject<PersonalStatsDto> replaySubject;


    public DefaultStatisticsController(RestApi restApi, Context context, AuthDao authDao,
                                       PerFormPersonalStatsDao perFormPersonalStatsDao) {
        this.restApi = restApi;
        this.context = context;
        this.authDao = authDao;
        this.perFormPersonalStatsDao = perFormPersonalStatsDao;
        this.replaySubject = ReplaySubject.create(1);
    }

    @Override
    public void setStats(PersonalStatsDto stats) {
        perFormPersonalStatsDao.setStats(POPULAR, stats.popular);
        perFormPersonalStatsDao.setStats(FILL_RATE, stats.fillRate);
        perFormPersonalStatsDao.setStats(FORMS_PER_USER, stats.formsPerUser);
        replaySubject.onNext(stats);
    }

    @Override
    public Observable<PersonalStatsDto> getPersonalStats(long formId) {
        Form form = new Form.Builder(formId).build();
        if (CommonUtils.isMobileNetworkConnected(context) || CommonUtils.isWifiConnected(context)) {
            sync(form).subscribeOn(RespoSchedulers.io())
                    .observeOn(RespoSchedulers.main())
                    .subscribe(syncResponse -> Log.d(SyncUtils.SYNC_TAG, "stats_synced"));
            return restApi.getStatisticsChartData(form).subscribeOn(RespoSchedulers.io())
                    .filter(stats -> stats != null)
                    .map(stat -> stat.stats);
        } else {
            return replaySubject;
        }
    }

    @Override
    public Observable<Syncable.SyncResult> sync(Form form) {
        return authDao.getAuth().map(auth -> restApi.getStatisticsChartData(form)
                .map(stats -> {
                    setStats(stats.stats);
                    return Syncable.SyncResult.SUCCESS;
                })).orElse(Observable.just(Syncable.SyncResult.FAIL));
    }
}