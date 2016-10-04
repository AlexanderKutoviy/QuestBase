package com.questbase.unittestssuite.controller;

import com.questbase.app.BuildConfig;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.resource.DefaultResourceController;
import com.questbase.app.controller.resource.ResourceController;
import com.questbase.app.dao.resource.ResourceDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;
import com.questbase.app.domain.State;
import com.questbase.app.net.RestApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class UnitResourceControllerTests {

    public static final int FORM_ID = 12;

    @Test
    public void testResourceSync() {
        RestApi restApi = mock(RestApi.class);
        ResourceDao resourceDao = mock(ResourceDao.class);
        FilesController filesController = mock(FilesController.class);
        Form form = new Form.Builder(FORM_ID)
                .title("title")
                .resources(getFormsFormResources())
                .state(State.OUTDATED).build();
//        stubs
        when(resourceDao.read(form)).thenReturn(getFormResourcesFromDb(form));
        for (FormResource formResource : getFormsFormResources()) {
            doNothing().when(resourceDao).update(formResource);
        }
        for (FormResource formResource : getFormsFormResourcesForCreation()) {
            doNothing().when(resourceDao).create(formResource);
        }

        ResourceController resourceController = new DefaultResourceController(resourceDao, restApi, filesController);
        resourceController.sync(form);
//        verification
        verify(resourceDao, times(5)).read(form);
    }

    public List<FormResource> getFormResourcesFromDb(Form form) {
        List<FormResource> formResources = new ArrayList<>();
        for (int i = 0; i < form.formId; i++) {
            formResources.add(new FormResource(String.valueOf(i), String.valueOf(i), State.OUTDATED, new Form.Builder(1).build()));
        }
        return formResources;
    }

    public List<FormResource> getFormsFormResources() {
        List<FormResource> formResources = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            formResources.add(new FormResource(String.valueOf(i), String.valueOf(i), State.OUTDATED, new Form.Builder(1).build()));
        }
        return formResources;
    }

    public List<FormResource> getFormsFormResourcesForCreation() {
        List<FormResource> formResources = new ArrayList<>();
        for (int i = 4; i < FORM_ID; i++) {
            formResources.add(new FormResource(String.valueOf(i), String.valueOf(i), State.OUTDATED, new Form.Builder(1).build()));
        }
        return formResources;
    }
}
