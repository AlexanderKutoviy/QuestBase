package com.questbase.app.controller.category;

import android.util.Log;

import com.annimon.stream.Stream;
import com.questbase.app.controller.Event;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.domain.State;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.Category;
import com.questbase.app.sync.SyncUtils;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class DefaultCategoryController implements CategoryController {

    private CategoryDao categoryDao;
    private final RestApi restApi;
    private PublishSubject<Event<Category>> observable;
    private final FormController formController;

    public DefaultCategoryController(CategoryDao categoryDao,
                                     RestApi restApi,
                                     FormController formController) {
        this.categoryDao = categoryDao;
        this.restApi = restApi;
        this.observable = PublishSubject.create();
        this.formController = formController;
    }

    @Override
    public void sync() {
        Log.d(SyncUtils.SYNC_TAG, "Categories sync");
        downloadAndSaveCategories();
        loadVersionedFormsForOutdatedCategories();
    }

    @Override
    public Observable<Event<Category>> observe() {
        return observable;
    }

    @Override
    public void addCategory(Category category) {
        categoryDao.create(category);
        observable.onNext(new Event<>(category, Event.Type.WRITE));
    }

    @Override
    public void deleteCategory(Category category) {
        categoryDao.delete(category);
        observable.onNext(new Event<>(category, Event.Type.DELETE));
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.update(category);
        observable.onNext(new Event<>(category, Event.Type.UPDATE));
    }

    @Override
    public List<Category> readAll() {
        return categoryDao.read();
    }

    private void downloadAndSaveCategories() {
        List<Category> categories = restApi.getCategories().toBlocking().first();
        deleteOutdatedCategories(categories);
        pushCategoriesToDb(categories);
    }

    private void pushCategoriesToDb(List<Category> categories) {
        Stream.of(categories).forEach(category -> {
            category.state = State.OUTDATED;
            addCategory(category);
        });
    }

    private void deleteOutdatedCategories(List<Category> categoriesFromServer) {
        List<Category> categoriesFromDb = categoryDao.read();
        categoriesFromDb.removeAll(categoriesFromServer);
        Stream.of(categoriesFromDb).forEach(this::deleteCategory);
    }

    private void loadVersionedFormsForOutdatedCategories() {
        Stream.of(categoryDao.read())
                .filter(category -> category.state.equals(State.OUTDATED))
                .forEach(outdated -> {
                    formController.sync(outdated);
                    outdated.state = State.UPDATED;
                    categoryDao.update(outdated);
                });
    }
}