package com.questbase.app.form.mvp;

import android.graphics.Typeface;
import android.view.View;

import com.google.gson.JsonObject;

interface FormView {
    View makeViewFromJson(JsonObject question);

    void setCurrentViewInSwiper(View currentView);

    void setNextViewInSwiper(View nextView);

    void showTitle(String formTitle);

    void setMyriadProRegularFont(Typeface font);
}
