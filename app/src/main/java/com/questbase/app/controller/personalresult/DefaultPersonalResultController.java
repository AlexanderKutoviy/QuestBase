package com.questbase.app.controller.personalresult;

import com.questbase.app.controller.personalresult.projectexamples.PortfolioController;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.WeeklyUserCountController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.PersonalResult;
import com.questbase.app.utils.Lu;
import com.questbase.app.utils.RespoSchedulers;


public class DefaultPersonalResultController implements PersonalResultController {

    private final RestApi restApi;
    private final AuthDao authDao;
    private final PortfolioController portfolioController;
    private final WeeklyUserCountController weeklyUserCountController;

    public DefaultPersonalResultController(PortfolioController portfolioController,
                                           WeeklyUserCountController weeklyUserCountController,
                                           RestApi restApi,
                                           AuthDao authDao) {
        this.portfolioController = portfolioController;
        this.weeklyUserCountController = weeklyUserCountController;
        this.restApi = restApi;
        this.authDao = authDao;
    }

    @Override
    public void sync() {
        restApi.getRespondentsChartData().subscribeOn(RespoSchedulers.io())
                .subscribe(this::fillDbViaController, Lu::handleTolerableException);
    }

    private void fillDbViaController(PersonalResult personalResult) {
        portfolioController.setPortfolio(personalResult.projects);
        weeklyUserCountController.setChartPoints(personalResult.sessions);
    }
}
