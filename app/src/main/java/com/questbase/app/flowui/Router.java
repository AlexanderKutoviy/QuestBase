package com.questbase.app.flowui;

import com.questbase.app.flowui.screens.RespoScreen;

public interface Router {
    void goTo(RespoScreen screen);

    void goToSettings();

    void exitApplication();
}