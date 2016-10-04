package com.questbase.app.di.modules;

import com.questbase.app.controller.testsession.DefaultTestSessionController;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.dao.testsession.DbFlowTestSessionDao;
import com.questbase.app.dao.testsession.TestSessionDao;
import com.questbase.app.net.RestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestSessionModule {

    @Singleton
    @Provides
    TestSessionController provideTestSessionController(TestSessionDao testSessionDao, RestApi restApi) {
        return new DefaultTestSessionController(testSessionDao, restApi);
    }

    @Singleton
    @Provides
    TestSessionDao provideTestSessionDao() {
        return new DbFlowTestSessionDao();
    }
}
