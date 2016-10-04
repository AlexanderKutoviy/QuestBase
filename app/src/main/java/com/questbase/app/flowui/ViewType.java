package com.questbase.app.flowui;

import com.annimon.stream.Stream;
import com.questbase.app.R;
import com.questbase.app.debugscreen.DebugScreen;
import com.questbase.app.feed.FeedScreen;
import com.questbase.app.flowui.screens.AuthorizationMailІScreen;
import com.questbase.app.flowui.screens.DeclinedPermissionScreen;
import com.questbase.app.flowui.screens.InternetWarningScreen;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.flowui.screens.RecoveryScreen;
import com.questbase.app.flowui.screens.RegisterByMailScreen;
import com.questbase.app.flowui.screens.RespoScreen;
import com.questbase.app.flowui.screens.WarningUpdateScreen;
import com.questbase.app.weblogin.WebLoginScreen;
import com.questbase.app.form.mvp.FormScreen;
import com.questbase.app.personalresult.PersonalResultScreen;
import com.questbase.app.profile.ProfileScreen;
import com.questbase.app.usage.screen.UsageScreen;

import java.util.Arrays;

enum ViewType {

    LOGIN(LoginScreen.class, R.layout.flow_login_screen),
    WEB_LOGIN(WebLoginScreen.class, R.layout.flow_web_login_screen),
    AUTHORIZATION_BY_MAIL(AuthorizationMailІScreen.class, R.layout.flow_authorization_mail_screen),
    REGISTER_BY_MAIL(RegisterByMailScreen.class, R.layout.flow_register_mail_screen),
    RECOVERY(RecoveryScreen.class, R.layout.flow_recovery_screen),
    INTERNET_WARNING(InternetWarningScreen.class, R.layout.flow_without_internet_con_screen),
    WARNING_UPDATE(WarningUpdateScreen.class, R.layout.flow_warning_update_screen),
    DECLINE_PERMISSION_VIEW(DeclinedPermissionScreen.class, R.layout.declined_permission_screen),
    FEED(FeedScreen.class, R.layout.flow_feed_screen),
    FORM(FormScreen.class, R.layout.flow_form_screen),
    PROFILE(ProfileScreen.class, R.layout.personal_cab_screen),
    PERSONAL_RESULT(PersonalResultScreen.class, R.layout.results_layout),
    USAGE(UsageScreen.class, R.layout.flow_usage_screen),
    DEBUG(DebugScreen.class, R.layout.flow_debug_screen);

    public final Class<? extends RespoScreen> screenClass;
    public final int viewLayoutId;

    ViewType(Class<? extends RespoScreen> screenClass, int viewLayoutId) {
        this.screenClass = screenClass;
        this.viewLayoutId = viewLayoutId;
    }

    public static ViewType fromScreenClass(Class<? extends RespoScreen> screenClass) {
        return Stream.of(Arrays.asList(ViewType.values()))
                .filter(viewType -> viewType.screenClass.equals(screenClass))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unrecognized screen " + screenClass.getSimpleName()));
    }
}