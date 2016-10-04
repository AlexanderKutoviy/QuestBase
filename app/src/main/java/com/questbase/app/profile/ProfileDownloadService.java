package com.questbase.app.profile;

import android.app.IntentService;
import android.content.Intent;

import com.questbase.app.CommonUtils;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.controller.profile.ProfileController;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerService;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.net.RestApi;
import com.questbase.app.utils.Lu;
import com.questbase.app.utils.RespoSchedulers;

import javax.inject.Inject;

public class ProfileDownloadService extends IntentService {

    @Inject
    RestApi restApi;
    @Inject
    ProfileController profileController;
    @Inject
    AuthDao authDao;

    public ProfileDownloadService() {
        super(ProfileDownloadService.class.getCanonicalName());
        DaggerProfileDownloadService_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (CommonUtils.isWifiConnected(this) || CommonUtils.isMobileNetworkConnected(this)) {
            if (authDao.isAuthorized()) {
                try {
                    restApi.getProfile().subscribeOn(RespoSchedulers.io())
                            .subscribe(event -> fillDbViaController(event, authDao),
                                    Lu::handleTolerableException);
                } catch (Exception e) {
                    Lu.handleTolerableException(e);
                }
            }
        }
    }

    protected void fillDbViaController(ProfileResponse profileResponse, AuthDao authDao) {
        authDao.getAuth().ifPresent(auth -> {
            profileResponse.userId = auth.userId;
            profileController.setProfile(profileResponse);
        });
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerService
    interface Component {
        void inject(ProfileDownloadService profileDownloadService);
    }
}