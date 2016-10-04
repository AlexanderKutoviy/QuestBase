package com.questbase.app.personalresult;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.annimon.stream.Stream;
import com.questbase.app.R;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.drawer.RespoDrawerView;
import com.questbase.app.net.entity.PortfolioDto;
import com.questbase.app.profile.ProfileScreen;
import com.questbase.app.utils.ChartUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import flow.Flow;
import lecho.lib.hellocharts.view.ColumnChartView;

public class DefaultPersonalResultView extends DrawerLayout implements PersonalResultView, RouterOwner {

    @Inject
    PersonalResultPresenter personalResultPresenter;
    private Router router;
    public static Map<ViewSwitcher, SwitcherState> switchers = new HashMap<>();

    public DefaultPersonalResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerDefaultPersonalResultView_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.menu_item).setOnClickListener(v ->
                ((RespoDrawerView) findViewById(R.id.left_drawer)).drawerLayout.openDrawer(Gravity.LEFT)
        );
        findViewById(R.id.personal_cab_btn).setOnClickListener(v -> router.goTo(new ProfileScreen()));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PersonalResultScreen personalResultScreen = Flow.getKey(this);
        personalResultPresenter.attachView(this, router,
                personalResultScreen != null ? personalResultScreen.getFormId() : 0);
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, personalResultPresenter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        personalResultPresenter.detachView();
    }

    @Override
    public void renderAvatar(Bitmap img) {
        ((ImageView) findViewById(R.id.header_avatar)).setImageBitmap(img);
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, personalResultPresenter);
    }

    @Override
    public void renderPopularityChart(float avg, float currentForm) {
        ChartUtils.createSmallChart(avg, currentForm, (ColumnChartView) findViewById(R.id.popularity_chart), getResources());
    }

    @Override
    public void renderPassedFormsChart(float avg, float users) {
        ChartUtils.createSmallChart(avg, users, (ColumnChartView) findViewById(R.id.passed_forms_chart), getResources());
    }

    @Override
    public void renderRespondentsAmount(List<String> points) {
        ChartUtils.createBigChart(points, (ColumnChartView) findViewById(R.id.respos_chart), getResources());
    }

    @Override
    public void renderProjectsExamples(List<PortfolioDto> portfolioDtos) {
        RecyclerView projectExamplesRecycler = (RecyclerView) findViewById(R.id.projects_recycler);
        PortfolioRecyclerAdapter portfolioRecyclerAdapter = new PortfolioRecyclerAdapter(portfolioDtos, personalResultPresenter);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        projectExamplesRecycler.setLayoutManager(horizontalLayoutManager);
        projectExamplesRecycler.setAdapter(portfolioRecyclerAdapter);
    }

    @Override
    public void setupListeners() {
        switchers.put((ViewSwitcher) findViewById(R.id.results_switcher), SwitcherState.OPENED);
        switchers.put((ViewSwitcher) findViewById(R.id.payment_switcher), SwitcherState.CLOSED);
        Stream.of(switchers).forEach(switcher ->
                switcher.getKey().setOnClickListener(v -> onClosedClickAction(switcher)));
    }

    private static void onClosedClickAction(Map.Entry<ViewSwitcher, SwitcherState> clickedSwitcher) {
        if (clickedSwitcher.getValue() == SwitcherState.CLOSED) {
            onClickSwitcher(clickedSwitcher);
            Stream.of(switchers)
                    .filter(switcher -> switcher.getValue() == SwitcherState.OPENED)
                    .forEach(openedSwitcher -> {
                        onClickSwitcher(openedSwitcher);
                        switchers.put(openedSwitcher.getKey(), SwitcherState.CLOSED);
                    });
            switchers.put(clickedSwitcher.getKey(), SwitcherState.OPENED);
        } else if (clickedSwitcher.getValue() == SwitcherState.OPENED) {
            onClickSwitcher(clickedSwitcher);
            switchers.put(clickedSwitcher.getKey(), SwitcherState.CLOSED);
        }
    }

    private static void onClickSwitcher(Map.Entry<ViewSwitcher, SwitcherState> switcher) {
        ViewGroup.LayoutParams nextParams = switcher.getKey().getNextView().getLayoutParams();
        ViewGroup.LayoutParams layoutParams = switcher.getKey().getLayoutParams();
        layoutParams.height = nextParams.height;
        layoutParams.width = nextParams.width;
        switcher.getKey().showNext();
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(DefaultPersonalResultView defaultPersonalResultView);
    }

    public enum SwitcherState {
        OPENED,
        CLOSED
    }
}