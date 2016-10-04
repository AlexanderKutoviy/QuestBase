package com.questbase.app.usage.screen;

import com.annimon.stream.Optional;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.permission.PermissionManager;
import com.questbase.app.presentation.LogoutPresenter;
import com.questbase.app.usage.controller.UsageController;

public class UsagePresenter implements LogoutPresenter {

    private final AuthDao authDao;
    private final UsageController usageController;
    private Router router;
    private UsageView view;
    private PermissionManager permissionManager;

    public UsagePresenter(PermissionManager permissionManager, UsageController usageController, AuthDao authDao) {
        this.authDao = authDao;
        this.permissionManager = permissionManager;
        this.usageController = usageController;
    }

    @Override
    public void onLogout() {
        getRouter().ifPresent(router -> {
            authDao.logout();
            router.goTo(new LoginScreen());
        });
    }

    public void attachView(UsageView view, Router router) {
        this.view = view;
        this.router = router;
        initView(view);
        view.displayAppUsages(usageController.getAppUsages());
    }

    public void detachView() {
        view = null;
        router = null;
    }

    private void initView(UsageView view) {
        view.setHasPermission(permissionManager.hasUsageStatsPermission());
    }

    private Optional<Router> getRouter() {
        return Optional.ofNullable(router);
    }
}
