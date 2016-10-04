package com.questbase.app.debugscreen;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.Category;
import com.questbase.app.presentation.LogoutPresenter;
import com.questbase.app.sync.SyncUtils;
import com.questbase.app.utils.Auth;
import com.questbase.app.utils.RespoSchedulers;
import com.questbase.app.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

public class DebugPresenter implements LogoutPresenter {

    private static final long REFRESH_TIME = 2; // 2 seconds

    private static final String TAG = DebugPresenter.class.getSimpleName();
    private final AuthDao authDao;
    private final QuestDao questDao;
    private final CategoryDao categoryDao;
    private final RestApi restApi;
    private Router router;
    private DebugView view;
    private Subscription viewUpdateSubscription;
    private Observable<DebugData> debugDataObservable;

    public DebugPresenter(AuthDao authDao, QuestDao questDao, CategoryDao categoryDao, RestApi restApi) {
        this.authDao = authDao;
        this.questDao = questDao;
        this.categoryDao = categoryDao;
        this.restApi = restApi;
        debugDataObservable = Observable
                .interval(REFRESH_TIME, TimeUnit.SECONDS)
                .onBackpressureLatest()
                .map(this::extractDebugData)
                .observeOn(RespoSchedulers.main());
    }

    public void onSyncClick() {
        Log.d(TAG, "Force sync");
        SyncUtils.triggerRefresh();
    }

    public void attachView(DebugView debugView, Router router) {
        this.router = router;
        this.view = debugView;
        renderDebugData(new DebugData());
        viewUpdateSubscription = debugDataObservable.subscribe(this::renderDebugData);
    }

    public void detachView() {
        view = null;
        router = null;
        if (viewUpdateSubscription != null) {
            viewUpdateSubscription.unsubscribe();
            viewUpdateSubscription = null;
        }
    }

    @Override
    public void onLogout() {
        authDao.logout();
        router.goTo(new LoginScreen());
    }

    public void onDumpStateClick() {
        debugDataObservable
                .takeFirst(debugData -> true)
                .subscribe(this::dumpState);
    }

    public void onGetMoneyClick() {
        restApi.mineSomeRespos()
                .subscribeOn(RespoSchedulers.io())
                .subscribe(response -> {
                    Log.d(TAG, "money_mining_response -> " + response);
                });
    }

    private void dumpState(DebugData debugData) {
        Log.d(TAG, "Dump state");
        StringBuilder dumpState = new StringBuilder()
                .append("Sync start time: ").append(debugData.syncStartTime).append(";\n")
                .append("Sync finish time: ").append(debugData.syncEndTime).append(";\n")
                .append("Is sync active: ").append(debugData.isSyncActive).append(";\n")
                .append("Categories amount: ").append(debugData.categoriesAmount).append(";\n")
                .append("User id: ").append(debugData.userId).append(";\n")
                .append("Token id: ").append(debugData.tokenId).append(";\n")
                .append("Token: ").append(debugData.token).append(";\n");
        Stream.of(debugData.formsInCategoriesCount.keySet())
                .forEach(category -> dumpState.append("Category: ").append(category.categoryName)
                        .append(": ").append(debugData.formsInCategoriesCount.get(category)).append("\n"));
        Log.d(TAG, dumpState.toString());
    }

    private void renderDebugData(DebugData debugData) {
        if (view != null) {
            view.cleanFormsAmount();
            view.renderVersionNumber(debugData.versionNumber);
            view.renderSyncStartTime(debugData.syncStartTime.map(TimeUtils::formatTime).orElse(""));
            view.renderSyncEndTime(debugData.syncEndTime.map(TimeUtils::formatTime).orElse(""));
            view.renderIsSyncActive(debugData.isSyncActive);
            view.renderCategoriesAmount(debugData.categoriesAmount);
            view.renderProfileInfo(debugData.tokenId, debugData.token, debugData.userId);
            Stream.of(debugData.formsInCategoriesCount.keySet())
                    .forEach(category -> view.renderFormsAmount(category, debugData.formsInCategoriesCount.get(category)));
        }
    }

    private DebugData extractDebugData(long time) {
        Optional<Auth> authOpt = authDao.getAuth();
        DebugData result = new DebugData();
        result.versionNumber = getVersionNumber();
        result.syncStartTime = getSyncStartTime();
        result.syncEndTime = getSyncFinishTime();
        result.isSyncActive = isSyncActive();
        result.categoriesAmount = getCategoriesAmount();
        result.token = authOpt.map(auth -> auth.token).orElse("");
        result.tokenId = authOpt.map(auth -> auth.tokenId).orElse("");
        result.userId = authOpt.map(auth -> auth.userId).orElse("");
        result.formsInCategoriesCount = Stream.of(categoryDao.read())
                .collect(Collectors.toMap(this::id, this::getFormsInCategoryAmount));

        return result;
    }

    private String getVersionNumber() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    private Optional<Long> getSyncStartTime() {
        return SyncUtils.getSyncStartTime();
    }

    private Optional<Long> getSyncFinishTime() {
        return SyncUtils.getSyncFinishTime();
    }

    private boolean isSyncActive() {
        return SyncUtils.isSyncActive();
    }

    private int getCategoriesAmount() {
        return (int) Stream.of(categoryDao.read()).count();
    }

    private Category id(Category c) {
        return c;
    }

    private int getFormsInCategoryAmount(Category category) {
        return (int) Stream.of(questDao.read(category)).count();
    }

    private String getUsersTokenId() {
        return authDao.getAuth().get().tokenId;
    }

    private String getUsersToken() {
        return authDao.getAuth().get().token;
    }

    private String getUserId() {
        return authDao.getAuth().get().userId;
    }

    private static class DebugData {
        String versionNumber;
        Optional<Long> syncStartTime = Optional.empty();
        Optional<Long> syncEndTime = Optional.empty();
        boolean isSyncActive;
        int categoriesAmount;
        String token = "";
        String tokenId = "";
        String userId = "";
        Map<Category, Integer> formsInCategoriesCount = new HashMap<>();
    }
}
