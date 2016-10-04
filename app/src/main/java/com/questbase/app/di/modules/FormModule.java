package com.questbase.app.di.modules;

import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.form.DefaultFormController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.controller.resource.ResourceController;
import com.questbase.app.dao.form.DbFlowQuestDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.net.RestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FormModule {

    @Singleton
    @Provides
    FormController provideFormController(RestApi restApi,
                                         QuestDao questDao,
                                         ResourceController resourceController,
                                         FilesController filesController) {
        return new DefaultFormController(restApi, questDao, resourceController, filesController);
    }

    @Singleton
    @Provides
    QuestDao provideFormDao() {
        return new DbFlowQuestDao();
    }
}
