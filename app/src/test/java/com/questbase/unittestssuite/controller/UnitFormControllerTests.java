package com.questbase.unittestssuite.controller;

import com.questbase.app.BuildConfig;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.form.DefaultFormController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.controller.resource.ResourceController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.form.QuestDao;
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

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class UnitFormControllerTests {

    private static final int FORMS_AMOUNT = 5;

    @Test
    public void syncTest() {
        RestApi restApi = mock(RestApi.class);
        AuthDao authDao = mock(AuthDao.class);
        QuestDao questDao = mock(QuestDao.class);
        FilesController filesController = mock(FilesController.class);
        ResourceController resourceController = mock(ResourceController.class);

        when(questDao.readAll()).thenReturn(getOutdatedFormsFromDb());
        when(authDao.isAuthorized()).thenReturn(true);
        doNothing().when(questDao).update(any(Form.class));
        doNothing().when(resourceController).sync(any(Form.class));
        for (Form outdated : getOutdatedFormsFromDb()) {
            when(restApi.getFormDescriptor(any(Form.class))).thenReturn(getFormDescriptor(outdated));
            when(resourceController.areAllResourcesUpdated(outdated)).thenReturn(true);
            outdated.state = State.UPDATED;
        }

        FormController formController = new DefaultFormController(restApi, questDao, resourceController, filesController);
        formController.sync();


        verify(questDao).readAll();
        verify(resourceController, times(FORMS_AMOUNT)).sync(any(Form.class));
        verify(questDao, times(FORMS_AMOUNT)).update(any(Form.class));
        for (Form outdated : getOutdatedFormsFromDb()) {
            verify(restApi).getFormDescriptor(outdated);
            Form form = getFormDescriptor(outdated).toBlocking().first();
            form.state = State.UPDATED;
        }
    }

    private static List<Form> getOutdatedFormsFromDb() {
        List<Form> allForms = new ArrayList<>();
        for (int i = 0; i < FORMS_AMOUNT; i++) {
            Form form = new Form.Builder(i).state(State.OUTDATED).build();
            allForms.add(form);
        }
        return allForms;
    }

    private static Observable<Form> getFormDescriptor(Form form) {
        form.version = 12;
        form.title = "form title";
        form.libVersion = "dfk213r";
        form.resources = getFormResources(form);
        return Observable.just(form);
    }

    private static List<FormResource> getFormResources(Form form) {
        List<FormResource> resources = new ArrayList<>();
        for (int i = 0; i < FORMS_AMOUNT; i++) {
            resources.add(new FormResource(String.valueOf(1), String.valueOf(i + 2), new Form.Builder(1).build()));
        }
        return resources;
    }
}