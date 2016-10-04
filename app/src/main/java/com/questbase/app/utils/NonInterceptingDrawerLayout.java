package com.questbase.app.utils;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonInterceptingDrawerLayout extends DrawerLayout {

    private boolean shouldInterceptEvents = true;

    public NonInterceptingDrawerLayout(Context context) {
        super(context);
    }

    public NonInterceptingDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonInterceptingDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setShouldInterceptEvents(boolean shouldInterceptEvents) {
        this.shouldInterceptEvents = shouldInterceptEvents;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event) && shouldInterceptEvents;
    }
}
