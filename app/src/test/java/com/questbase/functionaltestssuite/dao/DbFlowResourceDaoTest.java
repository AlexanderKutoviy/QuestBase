package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.resource.DbFlowResourceDao;
import com.questbase.app.dao.resource.ResourceDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowResourceDaoTest {

    @Test
    public void createReadResourceTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        ResourceDao resourceDao = new DbFlowResourceDao();
        List<FormResource> resources = getFormResourcesList(5);
        for (FormResource resource : resources) {
            resourceDao.create(resource);
        }
        List<FormResource> formResources = resourceDao.getAllResources();
        assertEquals(resources, formResources);
        FlowManager.destroy();
    }

    @Test
    public void createUpdateTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        ResourceDao resourceDao = new DbFlowResourceDao();
        List<FormResource> resources = getFormResourcesList(5);
        for (FormResource resource : resources) {
            resourceDao.create(resource);
        }
        assertEquals(resources, resourceDao.getAllResources());

        resourceDao.update(new FormResource("3", "12", new Form.Builder(1).build()));
        resources.set(3, new FormResource("3", "12", new Form.Builder(1).build()));
        assertEquals(resources, resourceDao.getAllResources());

        FlowManager.destroy();
    }

    @Test
    public void createDeleteTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        ResourceDao resourceDao = new DbFlowResourceDao();
        List<FormResource> resources = getFormResourcesList(5);
        for (FormResource resource : resources) {
            resourceDao.create(resource);
        }
        assertEquals(resources, resourceDao.getAllResources());

        resourceDao.delete(new FormResource("3", "3", new Form.Builder(1).build()));
        resources.remove(3);
        assertEquals(resources, resourceDao.getAllResources());

        FlowManager.destroy();
    }

    private static List<FormResource> getFormResourcesList(int count) {
        List<FormResource> formResources = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            formResources.add(new FormResource(String.valueOf(i), String.valueOf(i), new Form.Builder(1).build()));
        }
        return formResources;
    }
}
