package com.questbase.app.flowui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.questbase.app.CommonUtils;
import com.questbase.app.R;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.controller.analytics.AnalyticsController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.modules.PerActivity;
import com.questbase.app.feed.FeedScreen;
import com.questbase.app.flowui.screens.DeclinedPermissionScreen;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.flowui.screens.RespoScreen;
import com.questbase.app.flowui.screens.WarningUpdateScreen;
import com.questbase.app.sync.SyncUtils;
import com.questbase.app.usage.UsageRegistrationService;
import com.questbase.app.utils.AssetsUtils;
import com.questbase.app.utils.PermissionUtils;
import com.questbase.app.utils.PrefsHelper;
import com.questbase.app.utils.RespoSchedulers;

import javax.inject.Inject;

import flow.Direction;
import flow.Dispatcher;
import flow.Flow;
import flow.History;
import flow.Traversal;
import flow.TraversalCallback;

public class MainFlowActivity extends AppCompatActivity implements Dispatcher {

    private View currentView;
    private Router flowRouter = new FlowRouter();
    @Inject
    AuthDao authDao;
    @Inject
    AnalyticsController analyticsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        UsageRegistrationService.startUsageRegistrationService(this.getApplicationContext());
        DaggerMainFlowActivity_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
        initFilesCopying();
        SyncUtils.createSyncAccount(this);
        setContentView(R.layout.flow_main_frame);
        CommonUtils.setOrientation(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onAuthorizedUserEnter();
    }

    @Override
    protected void attachBaseContext(Context baseContext) {
        baseContext = Flow.configure(baseContext, this)
                .dispatcher(this)
                .defaultKey(new LoginScreen())
                .install();
        super.attachBaseContext(baseContext);
    }

    @Override
    public void onBackPressed() {
        Flow flow = Flow.get(getBaseContext());
        if (authDao.isAuthorized()) {
            if (Flow.get(this).getHistory().iterator().next() instanceof FeedScreen) {
                super.onBackPressed();
            } else {
                flow.replaceTop(new FeedScreen(), Direction.FORWARD);
            }
        } else {
            if (!Flow.get(this).goBack()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
        RespoScreen newScreen = traversal.destination.top();
        ViewGroup frame = (ViewGroup) findViewById(R.id.main_frame);

        if (traversal.origin != null) {
            if (frame.getChildCount() > 0) {
                traversal.getState(traversal.origin.top()).save(frame.getChildAt(0));
                frame.removeAllViews();
            }
        }
        ViewType viewType = ViewType.fromScreenClass(newScreen.getClass());
        View incomingView = LayoutInflater.from(traversal.createContext(newScreen, this))
                .inflate(viewType.viewLayoutId, frame, false);
        ((RouterOwner) incomingView).injectRouter(flowRouter);
        traversal.getState(traversal.destination.top()).restore(incomingView);
        frame.removeAllViews();
        frame.addView(incomingView);
        callback.onTraversalCompleted();

        currentView = incomingView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initFilesCopying();
                } else {
                    // Permission Denied
                    flowRouter.goTo(new DeclinedPermissionScreen());
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void onAuthorizedUserEnter() {
        Flow flow = Flow.get(getBaseContext());
        if (authDao.isAuthorized()) {
            if (Flow.get(this).getHistory().iterator().next() instanceof LoginScreen) {
                SyncUtils.triggerRefresh();
                if (!(CommonUtils.isMobileNetworkConnected(this) || CommonUtils.isWifiConnected(this))) {
                    flow.replaceTop(new WarningUpdateScreen(), Direction.FORWARD);
                } else {
                    flow.replaceTop(new FeedScreen(), Direction.FORWARD);
                }
            }
        }
    }

    private void initFilesCopying() {
        if (!PermissionUtils.checkWriteExternalStoragePermission(this)) {
            PermissionUtils.requestWriteExternalStoragePermission(this);
            return;
        }
        PrefsHelper prefsHelper = QuestBaseApplication.getAppComponent().getPrefsHelper();
        if (!prefsHelper.isDbImported()) {
            AssetsUtils.copyDatabase(getApplicationContext(), prefsHelper);
        } else {
            if (needsDbMigration(prefsHelper)) {
                RespoDatabase.migrate();
                prefsHelper.setDbVersion(RespoDatabase.VERSION);
            }
        }
        if (!prefsHelper.isContentImported()) {
            RespoSchedulers.io()
                    .createWorker()
                    .schedule(() -> AssetsUtils.copyForms(getApplicationContext(), prefsHelper, analyticsController));
        }
    }

    private boolean needsDbMigration(PrefsHelper prefsHelper) {
        return RespoDatabase.VERSION != prefsHelper.getDbVersion();
    }

    private class FlowRouter implements Router {

        @Override
        public void goTo(RespoScreen screen) {
            View view = currentView;
            final History.Builder newHistoryBuilder = Flow.get(view).getHistory().buildUpon();
            newHistoryBuilder.push(screen);
            Flow.get(view).setHistory(newHistoryBuilder.build(), Direction.BACKWARD);
        }

        public void goToSettings() {
            final Intent goToSettingsIntent = new Intent();
            goToSettingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            goToSettingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
            goToSettingsIntent.setData(Uri.parse("package:" + MainFlowActivity.this.getPackageName()));
            goToSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            goToSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            goToSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            MainFlowActivity.this.startActivity(goToSettingsIntent);
        }

        public void exitApplication() {
            MainFlowActivity.this.finish();
        }
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerActivity
    interface Component {
        void inject(MainFlowActivity mainFlowActivity);
    }
}