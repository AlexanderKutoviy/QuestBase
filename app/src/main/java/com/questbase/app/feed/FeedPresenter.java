package com.questbase.app.feed;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.controller.category.CategoryController;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.feed.feedcategories.CategoryType;
import com.questbase.app.feed.feedforms.FormItem;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.form.mvp.FormScreen;
import com.questbase.app.net.entity.Category;
import com.questbase.app.obsolete.ScriptManager;
import com.questbase.app.presentation.LogoutPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeedPresenter implements LogoutPresenter {
    private final String TAG = FeedPresenter.class.getSimpleName();
    private static final String ALL_CATEGORIES = "Пошук закатегоріями";
    private final AuthDao authDao;
    private final FormController formController;
    private final FilesController filesController;
    private FeedView view;
    private Router router;
    private List<Category> categories;
    private List<FormItem> forms = new ArrayList<>();
    private ScriptManager scriptManager;

    public FeedPresenter(CategoryController categoryController, AuthDao authDao,
                         FormController formController, FilesController filesController,
                         ScriptManager scriptManager) {
        this.authDao = authDao;
        this.formController = formController;
        this.filesController = filesController;
        categories = categoryController.readAll();
        this.scriptManager = scriptManager;
        initForms();
    }

    void attachView(FeedView view, Router router) {
        this.view = view;
        this.router = router;
        view.setCategories(initCategories());
        view.setNothingToShow(forms.isEmpty());
        view.setForms(forms);
        displayAvatarBitmap();
    }

    void detachView() {
        view = null;
        router = null;
    }

    private List<CategoryType> initCategories() {
        return Stream.of(categories).map(category ->
                Stream.of(CategoryType.values())
                        .filter(categoryType -> category.categoryId == categoryType.getCategoryId())
                        .map(categoryType -> categoryType)
                        .findFirst().get())
                .collect(Collectors.toList());
    }

    private void initForms() {
        forms = Stream.of(formController.getAllUpdatedForms())
                .map(form -> new FormItem(form.formId, form.version, form.title,
                        form.libVersion, form.category))
                .collect(Collectors.toList());
    }

    void onCategorySelected(CategoryType categoryType) {
        view.setSearchTitle(categoryType.getName());
        List<FormItem> update = Stream.of(forms)
                .filter(value -> value.getCategoryType().getCategoryId() == categoryType.getCategoryId())
                .collect(Collectors.toList());
        view.setNothingToShow(update.isEmpty());
        view.setForms(update);
        view.showClearIcon(true);
    }

    private void displayAvatarBitmap() {
        File avatar = filesController.getUsersAvatarPath();
        if (!avatar.exists()) {
            return;
        }
        view.renderAvatar(BitmapFactory.decodeFile(avatar.getAbsolutePath()));
    }

    void onFormSelected(FormItem form) {
        view.setLoading(true);
        scriptManager.getModel(filesController, (int) form.getFormId(), form.getLibVersion())
                .doOnError(model -> {
                    Log.e(TAG, "Can't get model " + model);
                    throw new RuntimeException("Can't get model " + model);
                }).subscribe(model -> view.showForm(form, model));
    }

    @Override
    public void onLogout() {
        authDao.logout();
        router.goTo(new LoginScreen());
    }

    void onClearAllSelect() {
        initForms();
        view.setForms(forms);
        view.setSearchTitle(ALL_CATEGORIES);
        view.showClearIcon(false);
    }

    void onLoadingEnd(FormItem form, ScriptManager.Model model) {
        view.setLoading(false);
        router.goTo(new FormScreen(form.getFormId(), form.getFormVersion(), form.getTitle(), model));
    }
}
