package com.questbase.app.swipeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

public class SwipeCard extends FrameLayout {
    private static final String FIRST_VIEW = "FIRST_VIEW";
    Animation animMove;
    Animation animZoom;
    Animation animFade;

    public SwipeCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeCard(Context context) {
        super(context);
    }

    public void setContentView(View contentView) {
        removeAllViews();
        if (contentView.getParent() != null) {
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
        addView(contentView);
    }

    public void setAnimMove(Animation animMove) {
        this.animMove = animMove;
    }

    public void setAnimZoom(Animation animZoom) {
        this.animZoom = animZoom;
    }

    public void setAnimFade(Animation animFade) {
        this.animFade = animFade;
    }

    public Animation getAnimZoom() {
        return animZoom;
    }

    public Animation getAnimMove() {
        return animMove;
    }

    public void startAnimMove() {
        this.startAnimation(animMove);
    }

    public void startAnimZoom() {
        this.startAnimation(animZoom);
    }

    public void startAnimFade() {
        this.startAnimation(animFade);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return getTag().equals(FIRST_VIEW) || super.onTouchEvent(event);
    }
}
