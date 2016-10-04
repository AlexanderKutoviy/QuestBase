package com.questbase.app.di.modules;

import android.content.Context;

import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.personalresult.DefaultPersonalResultController;
import com.questbase.app.controller.personalresult.PersonalResultController;
import com.questbase.app.controller.personalresult.projectexamples.DefaultPortfolioController;
import com.questbase.app.controller.personalresult.projectexamples.PortfolioController;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.DefaultWeeklyUserCountController;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.WeeklyUserCountController;
import com.questbase.app.controller.personalresult.statisticscharts.DefaultStatisticsController;
import com.questbase.app.controller.personalresult.statisticscharts.StatisticsController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.personalresult.personalstats.DbFlowPerFormPersonalStatsDao;
import com.questbase.app.dao.personalresult.personalstats.PerFormPersonalStatsDao;
import com.questbase.app.dao.personalresult.portfolio.DbFlowPortfolioDao;
import com.questbase.app.dao.personalresult.portfolio.PortfolioDao;
import com.questbase.app.dao.personalresult.weeklyusercount.DbFlowWeeklyUserCountDao;
import com.questbase.app.dao.personalresult.weeklyusercount.WeeklyUserCountDao;
import com.questbase.app.net.RestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonalResultModule {

    @Singleton
    @Provides
    PersonalResultController providePersonalResultController(PortfolioController portfolioController,
                                                             WeeklyUserCountController weeklyUserCountController,
                                                             RestApi restApi,
                                                             AuthDao authDao) {
        return new DefaultPersonalResultController(portfolioController, weeklyUserCountController, restApi, authDao);
    }

    @Singleton
    @Provides
    WeeklyUserCountController provideRespondentsChartController(WeeklyUserCountDao weeklyUserCountDao) {
        return new DefaultWeeklyUserCountController(weeklyUserCountDao);
    }

    @Singleton
    @Provides
    PortfolioController provideProjectExampleController(PortfolioDao portfolioDao, FilesController filesController) {
        return new DefaultPortfolioController(portfolioDao, filesController);
    }

    @Singleton
    @Provides
    StatisticsController provideStatistcsController(RestApi restApi, Context context, AuthDao authDao,
                                                    PerFormPersonalStatsDao perFormPersonalStatsDao) {
        return new DefaultStatisticsController(restApi, context, authDao, perFormPersonalStatsDao);
    }

    @Singleton
    @Provides
    WeeklyUserCountDao provideRespondentsChartDao() {
        return new DbFlowWeeklyUserCountDao();
    }

    @Singleton
    @Provides
    PortfolioDao provideProjectExampleDao() {
        return new DbFlowPortfolioDao();
    }

    @Singleton
    @Provides
    PerFormPersonalStatsDao providePerFormPersonalStatsDao() {
        return new DbFlowPerFormPersonalStatsDao();
    }
}