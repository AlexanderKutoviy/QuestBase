package com.questbase.app.flowui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.questbase.app.R;
import com.questbase.app.feed.FeedScreen;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;

public class WarningUpdateView extends RelativeLayout implements RouterOwner {

    private Router router;

    public WarningUpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewById(R.id.skip_btn).setOnClickListener(view -> router.goTo(new FeedScreen()));
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
    }
}
