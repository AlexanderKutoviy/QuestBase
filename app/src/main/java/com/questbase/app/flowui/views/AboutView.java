package com.questbase.app.flowui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

public class AboutView extends RelativeLayout {
    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Button b = new Button(getContext());
        b.setText("about");
    }
}
