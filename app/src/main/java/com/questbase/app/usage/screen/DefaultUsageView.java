package com.questbase.app.usage.screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.questbase.app.R;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.drawer.RespoDrawerView;
import com.questbase.app.usage.AppUsage;
import com.questbase.app.utils.TimeUtils;

import java.util.List;

import javax.inject.Inject;

public class DefaultUsageView extends DrawerLayout implements UsageView, RouterOwner {

    private static final String TAG = DefaultUsageView.class.getSimpleName();
    private Router router;
    @Inject
    UsagePresenter presenter;

    public DefaultUsageView(Context context) {
        super(context);
    }

    public DefaultUsageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultUsageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        DaggerDefaultUsageView_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
        findViewById(R.id.usage_permission_button).setOnClickListener(
                this::requestStatsPermission);
        ((RecyclerView) findViewById(R.id.usage_apps_list))
                .setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.attachView(this, router);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView();
    }

    @Override
    public void injectRouter(Router router) {
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, presenter);
        this.router = router;
    }

    @Override
    public void setHasPermission(boolean hasPermission) {
        int usagePermissionStringId = hasPermission
                ? R.string.usage_permission_on : R.string.usage_permission_off;
        ((TextView) findViewById(R.id.usage_permission_status)).setText(usagePermissionStringId);
        findViewById(R.id.usage_permission_button).setEnabled(!hasPermission);
    }

    @Override
    public void displayAppUsages(List<AppUsage> appUsages) {
        ((RecyclerView) findViewById(R.id.usage_apps_list))
                .setAdapter(new ListAdapter(appUsages));
    }

    @SuppressLint("NewApi")
    private void requestStatsPermission(View view) {
        getContext().startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    private class ListAdapter extends RecyclerView.Adapter<AppUsageItemHolder> {

        private List<AppUsage> appUsages;

        ListAdapter(List<AppUsage> appUsages) {
            this.appUsages = appUsages;
        }

        @Override
        public AppUsageItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AppUsageItemHolder();
        }

        @Override
        public void onBindViewHolder(AppUsageItemHolder holder, int position) {
            AppUsage appUsage = appUsages.get(position);
            holder.timeView.setText(TimeUtils.formatTime(appUsage.utcTime));
            holder.appNameView.setText(appUsage.packageName);
            try {
                holder.appIconView.setImageDrawable(
                        getContext().getPackageManager().getApplicationIcon(appUsage.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(TAG, "Couldn't find image for package: " + appUsage.packageName);
            }
        }

        @Override
        public int getItemCount() {
            return appUsages.size();
        }
    }

    private class AppUsageItemHolder extends RecyclerView.ViewHolder {

        TextView timeView;
        TextView appNameView;
        ImageView appIconView;

        public AppUsageItemHolder() {
            super(LayoutInflater.from(getContext()).inflate(R.layout.app_usage_item, null));
            timeView = (TextView) itemView.findViewById(R.id.app_usage_time);
            appNameView = (TextView) itemView.findViewById(R.id.app_package_name);
            appIconView = (ImageView) itemView.findViewById(R.id.app_image);
        }
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(DefaultUsageView view);
    }
}