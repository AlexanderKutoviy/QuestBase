package com.questbase.app.flowui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.questbase.app.R;

public class DrawerOptionElement extends FrameLayout {
    public DrawerOptionElement(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerOptionElement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        setClickable(true);
        setBackgroundResource(R.drawable.drawer_option_background);
        addView(LayoutInflater.from(
                getContext()).inflate(R.layout.drawer_option_element, this, false));
        TypedArray attributesArray = null;
        try {
            attributesArray = getContext().getTheme().obtainStyledAttributes(
                    attrs, R.styleable.DrawerOptionElement, defStyleAttr, 0);
            String optionText = attributesArray.getString(
                    R.styleable.DrawerOptionElement_option_text);
            ((TextView) findViewById(R.id.drawer_option_text)).setText(optionText);
        } finally {
            if (attributesArray != null) {
                attributesArray.recycle();
            }
        }
    }

    public void renderOptionSelected(boolean selected) {
        findViewById(R.id.drawer_option_indicator).setVisibility(
                selected ? View.VISIBLE : View.INVISIBLE);
    }
}
