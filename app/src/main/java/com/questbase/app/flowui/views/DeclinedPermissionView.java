package com.questbase.app.flowui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.questbase.app.R;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.utils.PermissionUtils;

public class DeclinedPermissionView extends RelativeLayout implements RouterOwner {

    private Router router;
    private Context context;

    public DeclinedPermissionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (PermissionUtils.checkWriteExternalStoragePermission(context)) {
            router.goTo(new LoginScreen());
        }
        findViewById(R.id.accept_btn).setOnClickListener(view -> router.goToSettings());
        findViewById(R.id.decline_btn).setOnClickListener(view -> router.exitApplication());
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
    }
}