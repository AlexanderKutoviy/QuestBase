package com.questbase.app.flowui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.questbase.app.CommonUtils;
import com.questbase.app.R;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.screens.AuthorizationMailІScreen;
import com.questbase.app.flowui.screens.InternetWarningScreen;
import com.questbase.app.utils.AuthUtils;
import com.questbase.app.weblogin.WebLoginScreen;

import java.util.HashMap;

public class LoginView extends RelativeLayout implements RouterOwner {

    private Router router;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        HashMap<Integer, String> urls = new HashMap<>();
        urls.put(R.id.fb_login_button, AuthUtils.createFbLoginUrl(getContext()));
        urls.put(R.id.vk_login_button, AuthUtils.createVkLoginUrl(getContext()));
        urls.put(R.id.gp_login_button, AuthUtils.createGpLoginUrl(getContext()));
        urls.put(R.id.ok_login_button, AuthUtils.createOkLoginUrl(getContext()));

        View.OnClickListener loginListener = v -> {
            if (!(CommonUtils.isMobileNetworkConnected(getContext())
                    || CommonUtils.isWifiConnected(getContext()))) {
                router.goTo(new InternetWarningScreen());
            } else if (v.getId() == R.id.mail_login_button) {
                router.goTo(new AuthorizationMailІScreen());
            } else {
                String url = urls.get(v.getId());
                router.goTo(new WebLoginScreen(url));
            }
        };

        for (int id : new int[]{R.id.gp_login_button, R.id.fb_login_button, R.id.vk_login_button,
                R.id.ok_login_button, R.id.mail_login_button}) {
            findViewById(id).setOnClickListener(loginListener);
        }
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
    }
}
