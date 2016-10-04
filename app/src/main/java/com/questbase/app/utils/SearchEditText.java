package com.questbase.app.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * 1. Hides keyboard on search.
 * 2. Gives focus up when back button is pressed.
 * 3. Restores previous edited value of no search was performed.
 */
public class SearchEditText extends EditText {
    private BackKeyListener backKeyListener;

    public SearchEditText(Context context) {
        super(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBackKeyListener(BackKeyListener backKeyListener) {
        this.backKeyListener = backKeyListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            clearFocus();
            if (backKeyListener != null) {
                backKeyListener.onBackKeyPressed();
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public interface BackKeyListener {
        void onBackKeyPressed();
    }
}