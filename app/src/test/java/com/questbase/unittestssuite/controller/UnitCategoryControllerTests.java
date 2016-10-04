package com.questbase.unittestssuite.controller;

import com.questbase.app.BuildConfig;
import com.questbase.app.controller.category.CategoryController;
import com.questbase.app.controller.category.DefaultCategoryController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.domain.State;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.Category;
import com.questbase.app.net.entity.VersionedForm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class UnitCategoryControllerTests {

    @Test
    public void categoriesDownloadTest() {
        CategoryDao categoryDao = mock(CategoryDao.class);
        RestApi restApi = mock(RestApi.class);
        FormController formController = mock(FormController.class);
//        stubs
        when(restApi.getCategories()).thenReturn(getCategories(6));
        when(categoryDao.read()).thenReturn(getCategoriesFromDb(2, 4));
        List<Category> categoriesForDeleting = getCategoriesForDeleting(
                getCategories(6).toBlocking().first(), getCategoriesFromDb(2, 4));
        for (Category category : categoriesForDeleting) {
            doNothing().when(categoryDao).delete(category);
        }
        for (Category category : getCategories(6).toBlocking().first()) {
            category.state = State.OUTDATED;
            doNothing().when(categoryDao).create(category);
        }

        CategoryController categoryController = new DefaultCategoryController(categoryDao, restApi, formController);
        categoryController.sync();
//      verification
        verify(restApi).getCategories();
        verify(categoryDao, times(2)).read();
        for (Category category : categoriesForDeleting) {
            verify(categoryDao).delete(category);
        }
        for (Category category : getCategories(6).toBlocking().first()) {
            category.state = State.OUTDATED;
            verify(categoryDao).create(category);
        }
    }

    @Test
    public void syncVersionedFormsTest() {
        CategoryDao categoryDao = mock(CategoryDao.class);
        RestApi restApi = mock(RestApi.class);
        QuestDao questDao = mock(QuestDao.class);
        FormController formController = mock(FormController.class);
//        stubs
        when(categoryDao.read()).thenReturn(getCategoriesFromDb(2, 4), getCategoriesFromDb(2, 4));
        for (Category category : getCategoriesFromDb(2, 4)) {
            when(restApi.getVersionedForms(category)).thenReturn(getVersionedFormsFromServer(category));
            doNothing().when(formController).sync(category);
        }
        when(restApi.getCategories()).thenReturn(getCategories(6));
        List<Category> categoriesForDeleting = getCategoriesForDeleting(
                getCategories(6).toBlocking().first(), getCategoriesFromDb(2, 4));
        for (Category category : categoriesForDeleting) {
            doNothing().when(categoryDao).delete(category);
        }
        for (Category category : getCategories(6).toBlocking().first()) {
            category.state = State.OUTDATED;
            doNothing().when(categoryDao).create(category);
        }

        CategoryController categoryController = new DefaultCategoryController(categoryDao, restApi, formController);
        categoryController.sync();
//      verification
        verify(categoryDao, times(2)).read();
        for (Category category : getCategoriesFromDb(2, 4)) {
            verify(formController).sync(category);
        }
    }

    private static Observable<List<Category>> getCategories(int count) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            categories.add(new Category(i, i, "name" + i));
        }
        return Observable.just(categories);
    }

    private static List<Category> getCategoriesFromDb(int start, int count) {
        List<Category> categories = new ArrayList<>();
        for (int i = start; i < count; i++) {
            categories.add(new Category(i, i, "name" + i, State.OUTDATED));
        }
        return categories;
    }

    private static List<Category> getCategoriesForDeleting(List<Category> categoriesFromServ, List<Category> categoriesFromDb) {
        categoriesFromDb.removeAll(categoriesFromServ);
        return categoriesFromDb;
    }

    private static Observable<List<VersionedForm>> getVersionedFormsFromServer(Category category) {
        List<VersionedForm> versionedForms = new ArrayList<>();
        long counter = category.categoryId;
        for (long i = counter; i < counter * 3; i++) {
            versionedForms.add(new VersionedForm(i, i));
        }
        return Observable.just(versionedForms);
    }
}