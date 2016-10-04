package com.questbase.app.debugscreen;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.questbase.app.R;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.drawer.RespoDrawerView;
import com.questbase.app.net.entity.Category;

import javax.inject.Inject;

public class DefaultDebugView extends DrawerLayout implements DebugView, RouterOwner {

    @Inject
    DebugPresenter debugPresenter;
    private Router router;

    public DefaultDebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerDefaultDebugView_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.sync_button).setOnClickListener(v -> debugPresenter.onSyncClick());
        findViewById(R.id.dump_state_button).setOnClickListener(v -> debugPresenter.onDumpStateClick());
        findViewById(R.id.mine_money_btn).setOnClickListener(v -> debugPresenter.onGetMoneyClick());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, debugPresenter);
        debugPresenter.attachView(this, router);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        debugPresenter.detachView();
    }

    @Override
    public void renderVersionNumber(String versionNumber) {
        ((TextView) findViewById(R.id.version_number))
                .setText(String.format(getResources().getString(R.string.debug_version_number), versionNumber));
    }

    @Override
    public void renderSyncStartTime(String syncStartTime) {
        ((TextView) findViewById(R.id.start_sync_time))
                .setText(String.format(getResources().getString(R.string.debug_sync_start_time), syncStartTime));
    }

    @Override
    public void renderSyncEndTime(String syncEndTime) {
        ((TextView) findViewById(R.id.end_sync_time))
                .setText(String.format(getResources().getString(R.string.debug_sync_finish_time), syncEndTime));
    }

    @Override
    public void renderIsSyncActive(boolean isSyncActive) {
        String text = getResources().getString(
                isSyncActive ? R.string.debug_sync_active : R.string.debug_sync_not_active);
        ((TextView) findViewById(R.id.is_sync_active)).setText(text);
    }

    @Override
    public void renderCategoriesAmount(long categoriesAmount) {
        ((TextView) findViewById(R.id.categories_amount))
                .setText(getResources().getString(R.string.debug_categories_amount, categoriesAmount));
    }

    @Override
    public void renderFormsAmount(Category category, long formsAmount) {
        LinearLayout formsLayout = (LinearLayout) findViewById(R.id.debug_screen_forms_layout);
        TextView tv = new TextView(getContext());
        tv.setText(getResources().getString(R.string.debug_forms_amount,
                category.categoryId, formsAmount));
        formsLayout.addView(tv);
    }

    @Override
    public void renderProfileInfo(String tokenId, String token, String userId) {
        String userIdText = String.format(getResources().getString(R.string.debug_user_id), userId);
        String tokeIdText = String.format(getResources().getString(R.string.debug_token_id), tokenId);
        String tokenText = String.format(getResources().getString(R.string.debug_token), token);
        ((TextView) findViewById(R.id.user_id)).setText(userIdText);
        ((TextView) findViewById(R.id.token_id)).setText(tokeIdText);
        ((TextView) findViewById(R.id.token)).setText(tokenText);
    }

    @Override
    public void cleanFormsAmount() {
        LinearLayout formsLayout = (LinearLayout) findViewById(R.id.debug_screen_forms_layout);
        formsLayout.removeAllViews();
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, debugPresenter);
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(DefaultDebugView defaultDebugView);
    }
}