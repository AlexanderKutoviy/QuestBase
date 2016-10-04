package com.questbase.app.debugscreen;

import com.questbase.app.flowui.screens.RespoScreen;

public class DebugScreen implements RespoScreen {
    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof DebugScreen;
    }
}
