package com.questbase.app.weblogin;

import android.view.View;
import android.webkit.WebView;

import com.questbase.app.controller.profile.ProfileController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.feed.FeedScreen;
import com.questbase.app.flowui.Router;
import com.questbase.app.sync.SyncUtils;
import com.questbase.app.utils.Auth;
import com.questbase.app.utils.Lu;
import com.questbase.app.utils.RespoSchedulers;

public class WebLoginPresenter {
    private static final String THERESPO_LOGIN_PREFIX = "https://therespo.com/user/login";
    private WebLoginView view;
    private Router router;
    private final AuthDao authDao;
    private final ProfileController profileController;

    public WebLoginPresenter(AuthDao authDao,
                             ProfileController profileController) {
        this.authDao = authDao;
        this.profileController = profileController;
    }

    void attachView(WebLoginView view, Router router) {
        this.view = view;
        this.router = router;
    }

    void detachView() {
        view = null;
    }

    void onAuthSuccess(String userId, String tokenId, String tokenValue, String url) {
        if (isTherespoUrl(url)) {
            authDao.setAuth(new Auth(userId, tokenValue, tokenId));
            profileController.sync()
                    .subscribeOn(RespoSchedulers.io())
                    .observeOn(RespoSchedulers.main())
                    .subscribe(syncResult -> router.goTo(new FeedScreen()));
            SyncUtils.triggerRefresh();
        }
    }

    void onAuthFail(String html, String url) {
        if (isTherespoUrl(url)) {
            Lu.handleTolerableException(new RuntimeException("Couldn't login: " + html));
        }
    }

    boolean isTherespoUrl(String url) {
        return url.startsWith(THERESPO_LOGIN_PREFIX);
    }

    public void onPagestarted(String url) {
        view.setVisibility(isTherespoUrl(url) ? View.INVISIBLE : View.VISIBLE);
    }

    public void onPageFinished(WebView view) {
        view.loadUrl("javascript:window.HtmlViewer.showHTML" +
                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }
}
