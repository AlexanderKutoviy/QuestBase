package com.questbase.app.swipeview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.annimon.stream.Stream;
import com.questbase.app.R;

import java.util.ArrayList;
import java.util.List;

public class SwiperView extends RelativeLayout {

    private static final String TAG = SwiperView.class.getSimpleName();
    private static final String FIRST_VIEW_TAG = "FIRST_VIEW";
    private static final String SECOND_VIEW_TAG = "SECOND_VIEW";
    private static final String THIRD_VIEW_TAG = "THIRD_VIEW";
    private static final String NEW_VIEW_TAG = "NEW_VIEW";

    SwipeCard firstView;
    SwipeCard secondView;
    SwipeCard thirdView;

    private List<OnNextViewListener> listeners = new ArrayList<>();

    private double viewHeight;
    private double viewWidth;

    State currentState;
    HandlerMoving handlerMove;

    RelativeLayout currentContentView;
    RelativeLayout nextContentView;

    private boolean canSwipe = false;

    public SwiperView(Context context) {
        super(context);
        handlerMove = new HandlerMoving(context);
        changeState(State.IDLE);
    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

    public boolean isCanSwipe() {
        return canSwipe;
    }

    public void addListener(OnNextViewListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnNextViewListener listener) {
        listeners.remove(listener);
    }

    public void setCurrentContentView(View currentView) {
        currentContentView = (RelativeLayout) currentView;
        if (currentContentView != null) {
            firstView.removeAllViews();
            firstView.setContentView(currentContentView);
        } else {
            Log.e(TAG, "ContainerView: null content");
        }
    }

    public void setNextContentView(View nextView) {
        if (nextView != null) {
            nextContentView = (RelativeLayout) nextView;
        }
    }

    public SwiperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handlerMove = new HandlerMoving(context);
        changeState(State.IDLE);

        viewHeight = getContext().getResources().getDimension(R.dimen.swiper_view_height);
        viewWidth = getContext().getResources().getDimension(R.dimen.swiper_view_width);

        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.zoom_index_height, outValue, true);
        float zoomIndexHeight = outValue.getFloat();
        getResources().getValue(R.dimen.zoom_index_width, outValue, true);
        float zoomIndexWidth = outValue.getFloat();

        firstView = setView(FIRST_VIEW_TAG, viewHeight, viewWidth, false);
        secondView = setView(SECOND_VIEW_TAG, (viewHeight / zoomIndexHeight),
                (viewWidth / zoomIndexWidth), true);
        thirdView = setView(THIRD_VIEW_TAG, (viewHeight / zoomIndexHeight),
                (viewWidth / zoomIndexWidth), true);
    }

    SwipeCard setView(String tag, double viewHeight,
                      double viewWidth, boolean isTouch) {

        SwipeCard inputCard = (SwipeCard) LayoutInflater.from(getContext()).
                inflate(R.layout.swipe_item, this, false);
        inputCard.setTag(tag);
        inputCard.setOnTouchListener((v, event) -> isTouch);

        if (!tag.equals(NEW_VIEW_TAG)) {
            this.viewHeight = viewHeight;
            this.viewWidth = viewWidth;
        }
        addToView(inputCard);
        return inputCard;
    }

    void addToView(View child) {
        LayoutParams layoutParams = new LayoutParams((int) viewWidth, (int) viewHeight);
        layoutParams.leftMargin = (int) getContext().getResources().
                getDimension(R.dimen.margin_left_card);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        child.setPadding((int) getContext().getResources().
                getDimension(R.dimen.swiper_card_padding_left), 0, 0, 0);
        addView(child, 0, layoutParams);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        currentState.dispatchTouchEvent(event, this);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //if user touch answers card doesn't move (y constrains)
        int upBorder = getResources().getInteger(R.integer.swiper_up_border);
        int bottomBorder = getResources().getInteger(R.integer.swiper_bottom_border);
        int tochDistance = getResources().getInteger(R.integer.swiper_touch_distance);
        if (event.getAction() == MotionEvent.ACTION_MOVE && currentState != State.IDLE) {
            if (Math.abs(handlerMove.getViewShiftX()) >= tochDistance
                    && (handlerMove.getFirstClickPosY() <= upBorder ||
                    handlerMove.getFirstClickPosY() >= bottomBorder)) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    public void moveToNextCard() {
        changeState(State.MOVING_OUT);
    }

    public interface OnNextViewListener {
        void onNextView();
    }

    void notifyNextView() {
        Stream.of(listeners).forEach(OnNextViewListener::onNextView);
    }

    void changeState(State stateIn) {
        currentState = stateIn;
        currentState.process(this);
    }

    enum State {
        IDLE {
            @Override
            public void process(SwiperView view) {
            }

            @Override
            public void dispatchTouchEvent(MotionEvent event, SwiperView view) {
                view.handlerMove.setMovePosX(event.getRawX());
                int upBorder = view.getResources().getInteger(R.integer.swiper_up_border);
                int bottomBorder = view.getResources().getInteger(R.integer.swiper_bottom_border);
                int tochDistance = view.getResources().getInteger(R.integer.swiper_touch_distance);
                if (event.getAction() == MotionEvent.ACTION_MOVE && view.canSwipe) {
                    if (Math.abs(view.handlerMove.getViewShiftX()) >= tochDistance
                            && (view.handlerMove.getFirstClickPosY() <= upBorder ||
                            view.handlerMove.getFirstClickPosY() >= bottomBorder)) {
                        view.changeState(State.DRAGGING);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.handlerMove.setFirstClickPosX(event.getRawX());
                    view.handlerMove.setFirstClickPosY(event.getY());
                }

            }
        },
        DRAGGING {
            @Override
            public void process(SwiperView view) {
                //TODO: RESPO-579 Make swiper rigth bihavior when dragging
//                if (view.canSwipe) {
//                    view.firstView.setTranslationX(view.handlerMove.getMovePosX()
//                            - view.handlerMove.getFirstClickPosX());
//                }
            }

            @Override
            void dispatchTouchEvent(MotionEvent event, SwiperView view) {
                view.handlerMove.setMovePosX(event.getRawX());
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    process(view);
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    if (Math.abs(view.handlerMove.getViewShiftX()) >= DISTANCE_TO_SWIPE
                            && view.isCanSwipe()
                            && view.handlerMove.getViewShiftX() <= 0) {
                        view.changeState(State.MOVING_OUT);
                        view.setCanSwipe(false);
                    } else {
                        view.changeState(State.MOVING_BACK);
                    }
                }
            }
        },
        MOVING_OUT {
            @Override
            public void process(SwiperView view) {
                view.handlerMove.onSwiped(view);
            }

            @Override
            void dispatchTouchEvent(MotionEvent event, SwiperView view) {
            }
        },
        MOVING_BACK {
            @Override
            public void process(SwiperView view) {
                view.handlerMove.createMoveToStartAnimation(view);
            }

            @Override
            void dispatchTouchEvent(MotionEvent event, SwiperView view) {
            }
        },
        ZOOMING {
            @Override
            public void process(SwiperView view) {
                view.handlerMove.createZoomAnimation(view.secondView, view.thirdView);
                view.handlerMove.makeZoomAnimation(view);
            }

            @Override
            void dispatchTouchEvent(MotionEvent event, SwiperView view) {
            }
        },
        APPEARING {
            @Override
            public void process(SwiperView view) {
                view.handlerMove.setAndStartAnimFade(view);
            }

            @Override
            void dispatchTouchEvent(MotionEvent event, SwiperView view) {
            }
        };
        float DISTANCE_TO_SWIPE = 50;

        abstract void process(SwiperView view);

        abstract void dispatchTouchEvent(MotionEvent event, SwiperView view);
    }
}
