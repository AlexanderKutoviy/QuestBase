package com.questbase.app.di.modules;

import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.resource.DefaultResourceController;
import com.questbase.app.controller.resource.ResourceController;
import com.questbase.app.dao.resource.DbFlowResourceDao;
import com.questbase.app.dao.resource.ResourceDao;
import com.questbase.app.net.RestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ResourceModule {

    @Singleton
    @Provides
    ResourceController provideResourceController(RestApi restApi, ResourceDao resourceDao, FilesController filesController) {
        return new DefaultResourceController(resourceDao, restApi, filesController);
    }

    @Singleton
    @Provides
    ResourceDao provideResourceDao() {
        return new DbFlowResourceDao();
    }
}
