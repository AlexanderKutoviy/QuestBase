package com.questbase.app.swipeview;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.questbase.app.R;
import com.questbase.app.swipeview.SwiperView.State;

class HandlerMoving {
    private static final String NEW_VIEW_TAG = "NEW_VIEW";
    private final Resources resources;

    private float firstClickPosX;
    private float firstClickPosY;
    private float movePosX;

    private Animation animFadeToContentView;

    public HandlerMoving(Context context) {
        resources = context.getResources();
    }

    public float getFirstClickPosY() {
        return firstClickPosY;
    }

    public float getFirstClickPosX() {
        return firstClickPosX;
    }

    public float getMovePosX() {
        return movePosX;
    }

    public void setFirstClickPosX(float firstClickPosX) {
        this.firstClickPosX = firstClickPosX;
    }

    public void setFirstClickPosY(float firstClickPosY) {
        this.firstClickPosY = firstClickPosY;
    }

    public void setMovePosX(float movePosX) {
        this.movePosX = movePosX;
    }

    void createMoveToStartAnimation(SwiperView container) {
        int durationAnimMoveToStart = resources.getInteger(R.integer.duration_anim_move_to_start);
        container.firstView.animate()
                .translationX(0)
                .translationY(0)
                .setDuration(durationAnimMoveToStart)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        container.changeState(State.IDLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    Animation createFadeAnimation() {
        int alphaFromFadeAnim = resources.getInteger(R.integer.alpha_from_fade_anim);
        int alphaToFadeAnim = resources.getInteger(R.integer.alpha_to_fade_anim);
        int durationFadeAnim = resources.getInteger(R.integer.duration_fade_anim);
        Animation animFade = new AlphaAnimation(alphaFromFadeAnim, alphaToFadeAnim);
        animFade.setInterpolator(new AccelerateInterpolator());
        animFade.setDuration(durationFadeAnim);
        return animFade;
    }

    void createZoomAnimation(SwipeCard secondView, SwipeCard thirdView) {
        TypedValue outValue = new TypedValue();
        resources.getValue(R.dimen.zoom_index_height, outValue, true);
        float zoomIndexHeight = outValue.getFloat();
        resources.getValue(R.dimen.zoom_index_width, outValue, true);
        float zoomIndexWidth = outValue.getFloat();
        resources.getValue(R.dimen.scale_from_y, outValue, true);
        float scaleFromY = outValue.getFloat();
        resources.getValue(R.dimen.scale_from_x, outValue, true);
        float scaleFromX = outValue.getFloat();
        resources.getValue(R.dimen.scale_x, outValue, true);
        float scaleX = outValue.getFloat();
        resources.getValue(R.dimen.scale_y, outValue, true);
        float scaleY = outValue.getFloat();
        int durationZoomAnim = resources.getInteger(R.integer.duration_zoom_anim);

        Animation animZoom = new ScaleAnimation(scaleFromX, zoomIndexWidth, scaleFromY,
                zoomIndexHeight, Animation.RELATIVE_TO_SELF, scaleX,
                Animation.RELATIVE_TO_SELF, scaleY);
        animZoom.setDuration(durationZoomAnim);

        Animation animZoomForThird = new ScaleAnimation(scaleFromX, zoomIndexWidth, scaleFromY,
                zoomIndexHeight, Animation.RELATIVE_TO_SELF, scaleX,
                Animation.RELATIVE_TO_SELF, scaleY);

        animZoomForThird.setDuration(durationZoomAnim);
        secondView.setAnimZoom(animZoom);
        thirdView.setAnimZoom(animZoomForThird);
    }

    void createMoveRightAnimation(SwipeCard firstView, Context swiperContext) {
        firstView.setAnimMove(AnimationUtils.loadAnimation(swiperContext,
                R.anim.move_right));
    }

    void createMoveLeftAnimation(SwipeCard firstView, Context swiperContext) {
        firstView.setAnimMove(AnimationUtils.loadAnimation(swiperContext,
                R.anim.move_left));
    }

    void makeZoomAnimation(SwiperView container) {
        RelativeLayout.LayoutParams lParamFirst = (RelativeLayout.LayoutParams)
                container.firstView.getLayoutParams();
        RelativeLayout.LayoutParams lParamSecond = (RelativeLayout.LayoutParams)
                container.secondView.getLayoutParams();
        RelativeLayout.LayoutParams lParamThird = (RelativeLayout.LayoutParams)
                container.thirdView.getLayoutParams();

        container.secondView.startAnimZoom();
        container.thirdView.startAnimZoom();

        container.secondView.getAnimZoom().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.removeView(container.firstView);

                container.firstView = container.secondView;
                if (container.firstView != null) {
                    container.firstView.removeAllViews();
                    container.firstView.setOnTouchListener((v, event) -> false);
                    setParamsAndClearAnimation(container.firstView, lParamFirst);
                    makeContentAndSetAnimation(container);
                }

                container.secondView = container.thirdView;
                if (container.secondView != null) {
                    setParamsAndClearAnimation(container.secondView, lParamSecond);
                }

                container.thirdView = container.setView(NEW_VIEW_TAG, 0, 0, true);
                setParamsAndClearAnimation(container.thirdView, lParamThird);
                container.changeState(State.APPEARING);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    void makeContentAndSetAnimation(SwiperView container) {
        animFadeToContentView = createFadeAnimation();
        container.currentContentView = container.nextContentView;
        container.currentContentView.setAnimation(animFadeToContentView);
        container.currentContentView.startAnimation(animFadeToContentView);
        createFadeAnimationListener(container);
        container.firstView.setContentView(container.currentContentView);
    }

    void setParamsAndClearAnimation(SwipeCard inputCard, RelativeLayout.LayoutParams lParams) {
        inputCard.setLayoutParams(lParams);
        inputCard.setAnimation(null);
    }

    void createFadeAnimationListener(SwiperView container) {
        animFadeToContentView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.notifyNextView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    void makeMoveAnimation(SwiperView container) {
        container.firstView.getAnimMove().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.removeView(container.firstView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        container.firstView.startAnimMove();
    }

    void onSwiped(SwiperView container) {
        createMoveLeftAnimation(container.firstView, container.getContext());
        makeMoveAnimation(container);
        container.changeState(State.ZOOMING);
        container.setCanSwipe(false);
    }

    float getViewShiftX() {
        return movePosX - firstClickPosX;
    }

    void setAndStartAnimFade(SwiperView container) {
        Animation animToThirdView = createFadeAnimation();
        animToThirdView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.changeState(State.IDLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        container.thirdView.setAnimFade(animToThirdView);
        container.thirdView.startAnimFade();
    }
}
