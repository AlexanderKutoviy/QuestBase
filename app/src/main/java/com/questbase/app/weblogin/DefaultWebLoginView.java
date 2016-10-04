package com.questbase.app.weblogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.questbase.app.CommonUtils;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import flow.Flow;

public class DefaultWebLoginView extends WebView implements WebLoginView, RouterOwner {
    private static final String HTML_VIEWER = "HtmlViewer";
    @Inject
    WebLoginPresenter presenter;
    private Router router;

    public DefaultWebLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerDefaultWebLoginView_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        WebLoginScreen screen = Flow.getKey(this);
        if (screen == null) {
            return;
        }
        presenter.attachView(this, router);
        // customize web view
        CookieManager.getInstance().removeAllCookie();
        setVisibility(View.INVISIBLE);
        getSettings().setJavaScriptEnabled(true);
        loadUrl(screen.getLoginUrl());
        addJavascriptInterface(new JavaScriptInterface(), HTML_VIEWER);
        setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, @NonNull SslErrorHandler handler, SslError error) {
                if (CommonUtils.IGNORE_CERTIFICATES) {
                    handler.proceed();
                } else {
                    super.onReceivedSslError(view, handler, error);
                }
            }

            public void onPageFinished(WebView view, String url) {
                presenter.onPageFinished(view);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                presenter.onPagestarted(url);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView();
    }

    class JavaScriptInterface {
        @JavascriptInterface
        public void showHTML(String html) {
            Pattern p = Pattern.compile("<html><head></head><body>\\{\"id\":" +
                    "(\\d+)" + // user id
                    ",\"tid\":\"" +
                    "(\\d+)" + // token id
                    "\",\"token\":\"" +
                    "([a-zA-Z0-9%]+)" + // token value
                    "\"\\}</body></html>");
            Matcher successMatcher = p.matcher(html);
            if (successMatcher.matches()) {
                String userId = successMatcher.group(1);
                String tokenId = successMatcher.group(2);
                String tokenValue = successMatcher.group(3);
                post(() -> presenter.onAuthSuccess(userId, tokenId,
                        tokenValue.replace("%2F", "/").replace("%2B", "+"), getUrl()));
            } else {
                post(() -> presenter.onAuthFail(html, getUrl()));
            }
        }
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(DefaultWebLoginView defaultWebLoginView);
    }
}
