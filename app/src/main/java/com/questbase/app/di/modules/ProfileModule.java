package com.questbase.app.di.modules;

import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.profile.DefaultProfileController;
import com.questbase.app.controller.profile.ProfileController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.profile.DbFlowProfileDao;
import com.questbase.app.dao.profile.ProfileDao;
import com.questbase.app.net.RestApi;
import com.questbase.app.utils.PrefsHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileModule {

    @Singleton
    @Provides
    public ProfileDao provideProfileDao() {
        return new DbFlowProfileDao();
    }

    @Singleton
    @Provides
    public ProfileController provideProfileController( ProfileDao profileDao,
                                                       AuthDao authDao,
                                                       RestApi restApi,
                                                       FilesController filesController,
                                                       PrefsHelper prefsHelper) {
        return new DefaultProfileController(filesController, profileDao, authDao, restApi, prefsHelper);
    }
}
