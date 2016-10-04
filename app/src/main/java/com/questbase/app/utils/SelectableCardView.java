package com.questbase.app.utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.questbase.app.R;

public class SelectableCardView extends CardView {
    public SelectableCardView(Context context) {
        super(context);
    }

    public SelectableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed()) {
            setCardBackgroundColor(getContext().getResources().getColor(R.color.grey_light_light));
        } else {
            setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
        }
    }
}
