package com.questbase.app.feed;

import com.questbase.app.flowui.screens.RespoScreen;

public class FeedScreen implements RespoScreen {
    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof FeedScreen;
    }
}
