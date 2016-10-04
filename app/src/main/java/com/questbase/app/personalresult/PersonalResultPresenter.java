package com.questbase.app.personalresult;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.personalresult.projectexamples.PortfolioController;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.WeeklyUserCountController;
import com.questbase.app.controller.personalresult.statisticscharts.StatisticsController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCount;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerPresenter;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.net.entity.PortfolioDto;
import com.questbase.app.presentation.LogoutPresenter;
import com.questbase.app.utils.RespoSchedulers;

import java.io.File;
import java.util.List;

import rx.Subscription;

public class PersonalResultPresenter implements LogoutPresenter {

    private final AuthDao authDao;
    private final WeeklyUserCountController weeklyUserCountController;
    private final PortfolioController portfolioController;
    private final StatisticsController statisticsController;
    private final FilesController filesController;
    private Subscription subscription;
    private Router router;
    private PersonalResultView personalResultView;

    public PersonalResultPresenter(AuthDao authDao,
                                   WeeklyUserCountController weeklyUserCountController,
                                   FilesController filesController,
                                   PortfolioController portfolioController,
                                   StatisticsController statisticsController) {
        this.authDao = authDao;
        this.weeklyUserCountController = weeklyUserCountController;
        this.portfolioController = portfolioController;
        this.filesController = filesController;
        this.statisticsController = statisticsController;
    }

    void attachView(PersonalResultView personalResultView, Router router, long formId) {
        this.personalResultView = personalResultView;
        this.router = router;
        loadResultPage(formId);
    }

    void detachView() {
        personalResultView = null;
        router = null;
        subscription.unsubscribe();
    }

    public void loadResultPage(long formId) {
        setRespondentsChart(weeklyUserCountController.getAllData());
        setProjectExamplesForms(portfolioController.getPortfolio());
        setAvatarBitmap();
        setSmallCharts(formId);
        personalResultView.setupListeners();
    }

    public void setProjectExamplesForms(List<PortfolioDto> portfolioDtos) {
        personalResultView.renderProjectsExamples(portfolioDtos);
    }

    public void setRespondentsChart(List<WeeklyUserCount> weeklyUserCounts) {
        personalResultView.renderRespondentsAmount(Stream.of(weeklyUserCounts)
                .map(week -> week.cnt).collect(Collectors.toList()));
    }

    public void setSmallCharts(long formId) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = statisticsController.getPersonalStats(formId)
                .observeOn(RespoSchedulers.main())
                .subscribe(statistic -> {
                    personalResultView.renderPopularityChart(statistic.popular.average, statistic.popular.mine);
                    personalResultView.renderPassedFormsChart(statistic.formsPerUser.average, statistic.formsPerUser.mine);
                });
    }

    public Bitmap setProjectAvatarBitmap(PortfolioDto portfolioDto) {
        File resourcePath = filesController.getProjectExamplesResourcesBasePath(portfolioDto.img);
        if (!resourcePath.exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(resourcePath.getAbsolutePath());
    }

    public void setAvatarBitmap() {
        File avatar = filesController.getUsersAvatarPath();
        if (!avatar.exists()) {
            return;
        }
        personalResultView.renderAvatar(BitmapFactory.decodeFile(avatar.getAbsolutePath()));
    }

    @Override
    public void onLogout() {
        authDao.logout();
        router.goTo(new LoginScreen());
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerPresenter
    interface Component {
        void inject(PersonalResultPresenter personalResultPresenter);
    }
}