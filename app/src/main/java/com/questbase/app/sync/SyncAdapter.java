package com.questbase.app.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.questbase.app.QuestBaseApplication;
import com.questbase.app.controller.analytics.AnalyticsController;
import com.questbase.app.controller.category.CategoryController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.controller.personalresult.PersonalResultController;
import com.questbase.app.controller.profile.ProfileController;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.controller.transaction.TransactionController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerService;
import com.questbase.app.net.RestApi;
import com.questbase.app.utils.PrefsHelper;
import com.questbase.app.utils.RespoSchedulers;

import javax.inject.Inject;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    @Inject
    CategoryController categoryController;
    @Inject
    FormController formController;
    @Inject
    TransactionController transactionController;
    @Inject
    PersonalResultController personalResultController;
    @Inject
    ProfileController profileController;
    @Inject
    AuthDao authDao;
    @Inject
    RestApi restApi;
    @Inject
    TestSessionController testSessionController;
    @Inject
    AnalyticsController analyticsController;
    @Inject
    PrefsHelper prefsHelper;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        DaggerSyncAdapter_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        if (authDao.isAuthorized()) {
            Log.d(SyncUtils.SYNC_TAG, "Sync started");
            SyncUtils.setSyncStartTime(System.currentTimeMillis());
            SyncUtils.setSyncFinishTime(null);
            profileController.sync()
                    .subscribeOn(RespoSchedulers.io())
                    .observeOn(RespoSchedulers.main())
                    .subscribe(syncResponse -> Log.d(SyncUtils.SYNC_TAG, "profile synced"));
            transactionController.sync();
            personalResultController.sync();
            categoryController.sync();
            formController.sync();
            testSessionController.sync();
            Log.d(SyncUtils.SYNC_TAG, "Sync finished");
            SyncUtils.setSyncFinishTime(System.currentTimeMillis());
            SyncUtils.setSyncDurationTime(SyncUtils.getSyncFinishTime().get() - SyncUtils.getSyncStartTime().get());
            analyticsController.logSyncDurationEvent(SyncUtils.getSyncDurationTime().get(), prefsHelper.getLastSyncTime());
            prefsHelper.setLastSyncTime(System.currentTimeMillis());
        } else {
            Log.d(SyncUtils.SYNC_TAG, "Sync is impossible. User must be authorized.");
        }
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerService
    interface Component {
        void inject(SyncAdapter syncAdapter);
    }
}