package com.questbase.app.flowui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;

public class RegisterByMailView extends RelativeLayout implements RouterOwner {

    public RegisterByMailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void injectRouter(Router router) {
    }
}