package com.questbase.app.feed;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.questbase.app.R;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.feed.feedcategories.CategoryAdapter;
import com.questbase.app.feed.feedcategories.CategoryType;
import com.questbase.app.feed.feedforms.FormItem;
import com.questbase.app.feed.feedforms.FormItemAdapter;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.drawer.RespoDrawerView;
import com.questbase.app.obsolete.ScriptManager;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultFeedView extends DrawerLayout implements FeedView, RouterOwner {
    private String TAPE_TAB = getResources().getString(R.string.questionnare);
    private String RESULT_TAB = getResources().getString(R.string.all_results);
    private boolean hiddenCategoryBlock = false;
    private boolean canHandleEvent = true;
    private Router router;
    private RecyclerView categoryRecycler;
    private FormItemAdapter formItemAdapter;
    //  private TabHost tabHost; TODO: RESPO-349 Display all results in tab

    @Inject
    FeedPresenter presenter;
    @Inject
    FilesController filesController;
    @Inject
    FormController formController;

    public DefaultFeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerDefaultFeedView_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP && canHandleEvent) {
            if (categoryRecycler.getVisibility() == VISIBLE) {
                categoryRecycler.setVisibility(INVISIBLE);
                hiddenCategoryBlock = true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            canHandleEvent = true;
            if (categoryRecycler.getVisibility() == INVISIBLE) {
                hiddenCategoryBlock = false;
            }
        } else {
            canHandleEvent = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setSearchTitle(String searchTitle) {
        ((TextView) findViewById(R.id.search_field)).setText(searchTitle);
    }

    @Override
    public void setForms(List<FormItem> list) {
        formItemAdapter.updateForms(list);
        formItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderAvatar(Bitmap img) {
        ((ImageView) findViewById(R.id.header_avatar)).setImageBitmap(img);
    }

    @Override
    public void setCategories(List<CategoryType> list) {
        categoryRecycler = (RecyclerView) findViewById(R.id.category_recycler);
        CategoryAdapter adapter = new CategoryAdapter(list);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        categoryRecycler.setAdapter(adapter);
        categoryRecycler.setItemAnimator(itemAnimator);
        categoryRecycler.setLayoutManager(layoutManager);
        adapter.addListener(category -> {
            categoryRecycler.setVisibility(INVISIBLE);
//            tabHost.setEnabled(true);
            presenter.onCategorySelected(category);
        });
    }

    @Override
    public void setNothingToShow(boolean isVisible) {
        TextView nothingToShowText = (TextView) findViewById(R.id.nothing_to_show);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_button);
        if (isVisible) {
            nothingToShowText.setVisibility(VISIBLE);
            floatingActionButton.setVisibility(INVISIBLE);
        } else {
            nothingToShowText.setVisibility(INVISIBLE);
            floatingActionButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void setLoading(boolean isLoading) {
        FrameLayout loader = (FrameLayout) findViewById(R.id.loader);
        ImageView loadImage = (ImageView) loader.findViewById(R.id.loader_image);
        if (isLoading) {
            loader.setVisibility(VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            loadImage.setAnimation(animation);
            loadImage.startAnimation(animation);
        } else {
            loadImage.clearAnimation();
            loader.setVisibility(GONE);
        }
    }

    @Override
    public void showClearIcon(boolean visible) {
        if (visible) {
            findViewById(R.id.clear_all).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.clear_all).setVisibility(GONE);
        }
    }

    @Override
    public void showForm(FormItem form, ScriptManager.Model model) {
        post(() -> presenter.onLoadingEnd(form, model));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //  tabHost = (TabHost) findViewById(android.R.id.tabhost);
        RespoDrawerView drawer = (RespoDrawerView) findViewById(R.id.left_drawer);
        findViewById(R.id.menu_item).setOnClickListener(v ->
                drawer.drawerLayout.openDrawer(Gravity.LEFT));
        //setUpTabs();
        findViewById(R.id.clear_all).setOnClickListener(v -> presenter.onClearAllSelect());
        setListenerOnTouchSearchField();
        setupFloatingButtonListener(createRecyclerForms());
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

//    private void setUpTabs() {
//        tabHost.setup();
//        setupTab(new TextView(getContext()), TAPE_TAB);
//        setupTab(new TextView(getContext()), RESULT_TAB);
//        tabHost.setOnTabChangedListener(this::onTabSelected);
//    }
//
//    private void setupTab(final View view, final String tag) {
//        TabHost.TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(
//                createTabView(tabHost.getContext(), tag)).setContent(tag1 -> view);
//        tabHost.addTab(setContent);
//    }

    private View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }

//    private void onTabSelected(String tabId) {
//        if (tabId.equals(RESULT_TAB)) {
//            tabHost.getTabWidget().setBackgroundResource(R.drawable.tab_result_active);
//        } else if (tabId.equals(TAPE_TAB)) {
//            tabHost.getTabWidget().setBackgroundResource(R.drawable.tab);
//        }
//    }

    private void setListenerOnTouchSearchField() {
        findViewById(R.id.search_icon).setOnTouchListener((view, motionEvent) -> {
            handleCategoryBlockVisibility();
            canHandleEvent = false;
            return false;
        });
        findViewById(R.id.search_field).setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                handleCategoryBlockVisibility();
                canHandleEvent = true;
            }
            return false;
        });
    }

    private void handleCategoryBlockVisibility() {
        if (categoryRecycler.getVisibility() == INVISIBLE && !hiddenCategoryBlock) {
            categoryRecycler.setVisibility(VISIBLE);
        } else {
            categoryRecycler.setVisibility(INVISIBLE);
            hiddenCategoryBlock = false;
        }
    }

    private RecyclerView createRecyclerForms() {
        RecyclerView formRecycler = (RecyclerView) findViewById(R.id.form_recycler);
        formItemAdapter = new FormItemAdapter(Collections.emptyList(), filesController, formController);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        formRecycler.setAdapter(formItemAdapter);
        formRecycler.setItemAnimator(itemAnimator);
        formRecycler.setLayoutManager(layoutManager);
        formItemAdapter.addListener(form -> presenter.onFormSelected(form));
        formRecycler.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                try {
                    formItemAdapter.viewBlockingQueue.put(view);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                formItemAdapter.viewBlockingQueue.remove(view);
            }
        });
        return formRecycler;
    }

    private void setupFloatingButtonListener(RecyclerView formRecycler) {
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(v -> {
            formRecycler.stopScroll();
            formRecycler.getLayoutManager().scrollToPosition(0);
        });
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, presenter);
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(DefaultFeedView defaultFeedView);
    }
}
