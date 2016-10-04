package com.questbase.app.flowui.drawer;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.R;
import com.questbase.app.debugscreen.DebugScreen;
import com.questbase.app.feed.FeedScreen;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.screens.RespoScreen;
import com.questbase.app.presentation.LogoutPresenter;
import com.questbase.app.profile.ProfileScreen;
import com.questbase.app.usage.screen.UsageScreen;
import com.questbase.app.utils.PrefsHelper;

import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;

public class RespoDrawerView extends ListView {

    private Router router;
    public DrawerLayout drawerLayout;
    private LogoutPresenter logoutPresenter;
    private PrefsHelper prefsHelper;
    private final String DEBUG = "Debug";
    private final String USAGE = "Usage";

    public RespoDrawerView(Context context) {
        super(context);
    }

    public RespoDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RespoDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(DrawerLayout drawerLayout, Router router, LogoutPresenter logoutPresenter) {
        this.router = router;
        this.logoutPresenter = logoutPresenter;
        this.drawerLayout = drawerLayout;
        this.prefsHelper = new PrefsHelper(getContext());
        initDrawerItems();
    }

    private void initDrawerItems() {
        List<String> itemNames = Stream.of(Arrays.asList(DrawerOption.values()))
                .map(DrawerOption::getItemStringId)
                .map(getResources()::getString)
                .collect(Collectors.toList());

        if (!prefsHelper.hasDebugRole()) {
            itemNames.remove(DEBUG);
            itemNames.remove(USAGE);
        }

        ArrayAdapter<String> drawerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.drawer_list_item, itemNames);
        setAdapter(drawerAdapter);
        setOnItemClickListener(this::onDrawerClick);
    }

    private void onDrawerClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout.closeDrawer(RespoDrawerView.this);
        DrawerOption.values()[position].action.call(this);
    }

    private void onLogoutClick() {
        prefsHelper.removeDebugRole();
        logoutPresenter.onLogout();
    }

    private static Action1<RespoDrawerView> createScreenHandler(RespoScreen respoScreen) {
        return respoDrawerView -> respoDrawerView.router.goTo(respoScreen);
    }

    private enum DrawerOption {
        PROFILE(R.string.drawer_profile_option, RespoDrawerView.createScreenHandler(new ProfileScreen())),
        FEED(R.string.drawer_feed_option, RespoDrawerView.createScreenHandler(new FeedScreen())),
        LOGOUT(R.string.drawer_logout_option, RespoDrawerView::onLogoutClick),
        DEBUG(R.string.drawer_debug_option, RespoDrawerView.createScreenHandler(new DebugScreen())),
        USAGE(R.string.drawer_usage_option, RespoDrawerView.createScreenHandler(new UsageScreen())),;

        private int itemStringId;
        private Action1<RespoDrawerView> action;

        DrawerOption(int itemStringId, Action1<RespoDrawerView> action) {
            this.action = action;
            this.itemStringId = itemStringId;
        }

        int getItemStringId() {
            return itemStringId;
        }
    }
}
