package com.questbase.app.di.modules;

import com.questbase.app.controller.category.CategoryController;
import com.questbase.app.controller.category.DefaultCategoryController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.dao.category.DbFlowCategoryDao;
import com.questbase.app.net.RestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CategoryModule {

    @Singleton
    @Provides
    CategoryController provideCategoryController(CategoryDao categoryDao, RestApi restApi, FormController formController) {
        return new DefaultCategoryController(categoryDao, restApi, formController);
    }

    @Singleton
    @Provides
    CategoryDao provideCategoryDao() {
        return new DbFlowCategoryDao();
    }
}
