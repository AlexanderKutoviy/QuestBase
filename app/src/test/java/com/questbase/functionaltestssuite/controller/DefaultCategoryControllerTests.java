package com.questbase.functionaltestssuite.controller;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.Event;
import com.questbase.app.controller.category.CategoryController;
import com.questbase.app.controller.category.DefaultCategoryController;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.form.DefaultFormController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.controller.resource.DefaultResourceController;
import com.questbase.app.controller.resource.ResourceController;
import com.questbase.app.dao.category.CategoryDao;
import com.questbase.app.dao.category.DbFlowCategoryDao;
import com.questbase.app.dao.form.DbFlowQuestDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.dao.resource.DbFlowResourceDao;
import com.questbase.app.dao.resource.ResourceDao;
import com.questbase.app.domain.State;
import com.questbase.app.net.MockRestApi;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.Category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DefaultCategoryControllerTests {

    @Test
    public void testCreateDeleteReadScenario() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSubscriber<Event<Category>> testSubscriber = new TestSubscriber<>();
        int eventsCount = 0;
        CategoryController defaultCategoryController = generateCategoryController();
        List<Category> categories = getCategoriesList();

        defaultCategoryController.observe().subscribe(testSubscriber);
        List<Event<Category>> list = testSubscriber.getOnNextEvents();
        //add transactions
        for (Category category : categories) {
            defaultCategoryController.addCategory(category);
            eventsCount++;
        }
        //delete transaction
        defaultCategoryController.deleteCategory(new Category(3, 122332432, "safas", State.OUTDATED));
        eventsCount++;

        testSubscriber.assertValueCount(eventsCount);
        List<Event<Category>> events = toWriteEventList(categories);
        events.add(new Event<>(new Category(3, 122332432, "safas", State.OUTDATED), Event.Type.DELETE));
        assertEquals(events, list);
        FlowManager.destroy();
    }


    @Test
    public void testCreateUpdateReadScenario() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSubscriber<Event<Category>> testSubscriber = new TestSubscriber<>();
        int eventsCount = 0;
        CategoryController defaultCategoryController = generateCategoryController();
        List<Category> categories = getCategoriesList();
        defaultCategoryController.observe().subscribe(testSubscriber);
        List<Event<Category>> list = testSubscriber.getOnNextEvents();

        //add transactions
        for (Category category : categories) {
            defaultCategoryController.addCategory(category);
            eventsCount++;
        }
        //update transaction
        defaultCategoryController.updateCategory(new Category(3, 111, "fasdf", State.OUTDATED));
        eventsCount++;

        testSubscriber.assertValueCount(eventsCount);
        List<Event<Category>> events = toWriteEventList(categories);
        events.add(new Event<>(new Category(3, 111, "fasdf", State.OUTDATED), Event.Type.UPDATE));
        assertEquals(events, list);
        FlowManager.destroy();
    }

    public static List<Category> getCategoriesList() {
        return Arrays.asList(new Category(1, 134, "sfsf", State.OUTDATED),
                new Category(2, 235235, "sdaf", State.OUTDATED),
                new Category(3, 122332432, "sdaf", State.OUTDATED),
                new Category(4, 234234, "sdaf", State.OUTDATED),
                new Category(6, 432442, "sdaf", State.OUTDATED));
    }

    public static List<Event<Category>> toWriteEventList(List<Category> categories) {
        List<Event<Category>> eventList = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            eventList.add(new Event<>(categories.get(i), Event.Type.WRITE));
        }
        return eventList;
    }

    public static CategoryController generateCategoryController() {
        RestApi restApi = new MockRestApi();
        FilesController filesController = mock(FilesController.class);
        CategoryDao categoryDao = new DbFlowCategoryDao();
        QuestDao questDao = new DbFlowQuestDao();
        ResourceDao resourceDao = new DbFlowResourceDao();
        ResourceController resourceController = new DefaultResourceController(resourceDao, restApi, filesController);
        FormController formController = new DefaultFormController(restApi, questDao, resourceController, filesController);
        return new DefaultCategoryController(categoryDao, restApi, formController);
    }
}
