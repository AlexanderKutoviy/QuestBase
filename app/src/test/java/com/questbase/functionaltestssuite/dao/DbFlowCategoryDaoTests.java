package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.dao.category.DbFlowCategoryDao;
import com.questbase.app.domain.State;
import com.questbase.app.net.entity.Category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowCategoryDaoTests {
    @Test
    public void testCreateReadCategoryTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        CategoryDao categoryDao = new DbFlowCategoryDao();
        List<Category> categories = getCategoriesList();
        for (Category category : categories) {
            categoryDao.create(category);
        }
        List<Category> categoriesFromDb = categoryDao.read();
        assertEquals(categories, categoriesFromDb);
        FlowManager.destroy();
    }

    @Test
    public void testCreateReadUpdateReadCategoryTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        CategoryDao categoryDao = new DbFlowCategoryDao();
        List<Category> categories = getCategoriesList();
        for (Category category : categories) {
            categoryDao.create(category);
        }

        List<Category> categoriesFromDb = categoryDao.read();
        assertEquals(categories, categoriesFromDb);

        categoryDao.update(new Category(3, 111111, "asd", State.OUTDATED));
        categories.set(2, new Category(3, 111111, "asd", State.OUTDATED));
        categoriesFromDb = categoryDao.read();
        assertEquals(categories, categoriesFromDb);
        FlowManager.destroy();
    }

    public static List<Category> getCategoriesList() {
        return Arrays.asList(new Category(1, 134, "sfsf", State.OUTDATED),
                new Category(2, 235235, "sdaf", State.OUTDATED),
                new Category(3, 122332432, "sdaf", State.OUTDATED),
                new Category(4, 234234, "sdaf", State.OUTDATED),
                new Category(6, 432442, "sdaf", State.OUTDATED));
    }
}
