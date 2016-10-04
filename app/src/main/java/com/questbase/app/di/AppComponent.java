package com.questbase.app.di;

import android.content.Context;

import com.questbase.app.cache.BitmapCache;
import com.questbase.app.controller.analytics.AnalyticsController;
import com.questbase.app.controller.category.CategoryController;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.controller.personalresult.PersonalResultController;
import com.questbase.app.controller.personalresult.projectexamples.PortfolioController;
import com.questbase.app.controller.personalresult.statisticscharts.StatisticsController;
import com.questbase.app.controller.personalresult.weeklyusercountcontroller.WeeklyUserCountController;
import com.questbase.app.controller.profile.ProfileController;
import com.questbase.app.controller.resource.ResourceController;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.controller.transaction.TransactionController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.dao.personalresult.personalstats.PerFormPersonalStatsDao;
import com.questbase.app.dao.testsession.TestSessionDao;
import com.questbase.app.dao.transaction.PayoutEventDao;
import com.questbase.app.dao.transaction.TransactionDao;
import com.questbase.app.debugscreen.DebugPresenter;
import com.questbase.app.di.modules.AnalyticsModule;
import com.questbase.app.di.modules.AndroidModule;
import com.questbase.app.di.modules.AuthModule;
import com.questbase.app.di.modules.CacheModule;
import com.questbase.app.di.modules.CategoryModule;
import com.questbase.app.di.modules.FileModule;
import com.questbase.app.di.modules.FormModule;
import com.questbase.app.di.modules.PersonalResultModule;
import com.questbase.app.di.modules.PresenterModule;
import com.questbase.app.di.modules.ProfileModule;
import com.questbase.app.di.modules.ResourceModule;
import com.questbase.app.di.modules.RestModule;
import com.questbase.app.di.modules.ScriptManagerModule;
import com.questbase.app.di.modules.TestSessionModule;
import com.questbase.app.di.modules.TransactionModule;
import com.questbase.app.di.modules.UsageModule;
import com.questbase.app.feed.FeedPresenter;
import com.questbase.app.form.mvp.FormPresenter;
import com.questbase.app.net.RestApi;
import com.questbase.app.obsolete.ScriptManager;
import com.questbase.app.personalresult.PersonalResultPresenter;
import com.questbase.app.profile.PersonalCabPresenter;
import com.questbase.app.usage.controller.UsageController;
import com.questbase.app.usage.screen.UsagePresenter;
import com.questbase.app.utils.PrefsHelper;
import com.questbase.app.weblogin.WebLoginPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AndroidModule.class, AuthModule.class, RestModule.class,
        CacheModule.class, ProfileModule.class, TransactionModule.class,
        CategoryModule.class, FormModule.class, ResourceModule.class, PersonalResultModule.class,
        PresenterModule.class, FileModule.class, UsageModule.class, TestSessionModule.class,
        AnalyticsModule.class, ScriptManagerModule.class})
public interface AppComponent {

    Context getContext();

    RestApi getRestApi();

    PrefsHelper getPrefsHelper();

    BitmapCache getBitmapCache();

    ProfileController getProfileController();

    TransactionController getTransactionController();

    QuestDao getFormDao();

    CategoryDao getCategoryDao();

    TransactionDao getTransactionDao();

    PayoutEventDao getPayoutEventDao();

    CategoryController getCategoryController();

    FormController getFormController();

    ResourceController getResourceController();

    UsageController getUsageController();

    PersonalResultController getPersonalResultController();

    WeeklyUserCountController getRespondentsChartController();

    StatisticsController getStatisticsController();

    PortfolioController getProjectExamplesController();

    AuthDao getAuthDao();

    UsagePresenter getUsagePresenter();

    DebugPresenter getDebugPresenter();

    FeedPresenter getFeedPresenter();

    FormPresenter getFormPresenter();

    PersonalCabPresenter getProfilePresenter();

    PersonalResultPresenter getPersonalResultPresenter();

    FilesController getFileController();

    WebLoginPresenter getWebLoginPresenter();

    TestSessionDao getTestSessionDao();

    TestSessionController getTestSessionController();

    AnalyticsController getAnalyticsController();

    ScriptManager getScriptManager();

    PerFormPersonalStatsDao getPerFormPersonalStatsDao();
}