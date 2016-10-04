package com.questbase.app.flowui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;

public class InternetWarningView extends RelativeLayout implements RouterOwner {

    public InternetWarningView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void injectRouter(Router router) {
    }
}
