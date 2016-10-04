package com.questbase.app.di.modules;

import com.questbase.app.controller.category.CategoryController;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.font.FontController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.controller.personalresult.projectexamples.PortfolioController;
import com.questbase.app.controller.personalresult.statisticscharts.StatisticsController;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.WeeklyUserCountController;
import com.questbase.app.controller.profile.ProfileController;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.controller.transaction.TransactionController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.debugscreen.DebugPresenter;
import com.questbase.app.feed.FeedPresenter;
import com.questbase.app.form.mvp.FormPresenter;
import com.questbase.app.net.RestApi;
import com.questbase.app.obsolete.ScriptManager;
import com.questbase.app.permission.PermissionManager;
import com.questbase.app.personalresult.PersonalResultPresenter;
import com.questbase.app.profile.PersonalCabPresenter;
import com.questbase.app.usage.controller.UsageController;
import com.questbase.app.usage.screen.UsagePresenter;
import com.questbase.app.weblogin.WebLoginPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Singleton
    @Provides
    DebugPresenter provideDebugPresenter(AuthDao authDao, QuestDao questDao, CategoryDao categoryDao,
                                         RestApi restApi) {
        return new DebugPresenter(authDao, questDao, categoryDao, restApi);
    }

    @Singleton
    @Provides
    FeedPresenter provideFeedPresenter(CategoryController categoryController,
                                       AuthDao authDao,
                                       FormController formController,
                                       FilesController filesController,
                                       ScriptManager scriptManager) {
        return new FeedPresenter(categoryController, authDao, formController, filesController, scriptManager);
    }

    @Singleton
    @Provides
    PersonalCabPresenter provideProfilePresenter(AuthDao authDao,
                                                 FilesController filesController,
                                                 ProfileController profileController,
                                                 TransactionController transactionController) {
        return new PersonalCabPresenter(authDao, filesController, profileController, transactionController);
    }

    @Singleton
    @Provides
    PersonalResultPresenter providePersonalResultPresenter(AuthDao authDao,
                                                           WeeklyUserCountController weeklyUserCountController,
                                                           FilesController filesController,
                                                           PortfolioController portfolioController,
                                                           StatisticsController statisticsController) {
        return new PersonalResultPresenter(authDao,
                weeklyUserCountController,
                filesController,
                portfolioController,
                statisticsController);
    }

    @Singleton
    @Provides
    UsagePresenter provideUsagePresenter(PermissionManager permissionManager,
                                         UsageController usageController, AuthDao authDao) {
        return new UsagePresenter(permissionManager, usageController, authDao);
    }

    @Singleton
    @Provides
    FormPresenter provideFormPresenter(FormController formController, FontController fontController,
                                       TestSessionController testSessionController) {
        return new FormPresenter(formController, fontController, testSessionController);
    }

    @Singleton
    @Provides
    WebLoginPresenter provideWebLoginPresenter(AuthDao authDao, ProfileController profileController) {
        return new WebLoginPresenter(authDao, profileController);
    }
}