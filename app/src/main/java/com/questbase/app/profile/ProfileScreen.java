package com.questbase.app.profile;

import com.questbase.app.flowui.screens.RespoScreen;

public class ProfileScreen implements RespoScreen {
    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof ProfileScreen;
    }
}