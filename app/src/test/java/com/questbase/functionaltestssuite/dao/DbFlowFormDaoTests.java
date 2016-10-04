package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.category.DbFlowCategoryDao;
import com.questbase.app.dao.form.DbFlowQuestDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.State;
import com.questbase.app.net.entity.Category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowFormDaoTests {

    private final static int FORMS_AMOUNT = 10;
    private final static Category CATEGORY = new Category(1, 1, "name", State.UPDATED);

    @Test
    public void testSetMultipleForms() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        new DbFlowCategoryDao().create(CATEGORY);
        QuestDao questDao = new DbFlowQuestDao();
        for (Form form : formsList()) {
            questDao.create(form);
        }

        assertEquals(formsList(), questDao.readAll());
        FlowManager.destroy();
    }

    @Test
    public void testSetAndUpdateForms() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        new DbFlowCategoryDao().create(CATEGORY);
        QuestDao questDao = new DbFlowQuestDao();
        for (Form form : formsList()) {
            questDao.create(form);
        }

        Form formToUpdate = new Form.Builder(4)
                .title("4")
                .resources(new ArrayList<>())
                .state(State.OUTDATED)
                .category(CATEGORY)
                .build();

        Form formToDelete = new Form.Builder(6)
                .title("6")
                .resources(new ArrayList<>())
                .state(State.UPDATED)
                .category(CATEGORY)
                .build();

        questDao.update(formToUpdate, CATEGORY);
        questDao.delete(formToDelete);

        List<Form> formsFromDb = questDao.readAll();

        assertNotEquals(formsList(), formsFromDb);
        assertEquals(formsFromDb.get(4), formToUpdate);
        assertEquals(formsFromDb.size(), FORMS_AMOUNT - 1);

        FlowManager.destroy();
    }

    private List<Form> formsList() {
        List<Form> forms = new ArrayList<>();
        for (int i = 0; i < FORMS_AMOUNT; i++) {
            forms.add(new Form.Builder(i)
                    .title(String.valueOf(i))
                    .resources(new ArrayList<>())
                    .state(State.UPDATED)
                    .category(CATEGORY)
                    .build());
        }
        return forms;
    }
}