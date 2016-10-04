package com.questbase.app.controller.resource;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.dao.resource.ResourceDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;
import com.questbase.app.domain.State;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.body.ResourceRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class DefaultResourceController implements ResourceController {

    ResourceDao resourceDao;
    RestApi restApi;
    FilesController filesController;
    private final String TAG = DefaultResourceController.class.getSimpleName();

    public DefaultResourceController(ResourceDao resourceDao, RestApi restApi, FilesController filesController) {
        this.resourceDao = resourceDao;
        this.restApi = restApi;
        this.filesController = filesController;
    }

    @Override
    public void sync(Form form) {
        setOutdatedResources(form);
        loadOutdatedResources(form);
    }

    @Override
    public InputStream getResourceStream(FormResource formResource) {
        try {
            return new FileInputStream(resourceDao.getFilePath(formResource));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean areAllResourcesUpdated(Form form) {
        return Stream.of(resourceDao.read(form))
                .filter(resource -> resource.state.equals(State.OUTDATED)).count() == 0;
    }

    private void setOutdatedResources(Form form) {
        Stream.of(form.resources).forEach(resource -> performResourceDatabaseSync(resource, resourceDao.read(form), form));
    }

    private void performResourceDatabaseSync(FormResource resource, List<FormResource> resourcesFromDb, Form form) {
        if (containsId(resource, resourcesFromDb)) {
            Stream.of(resourcesFromDb).forEach(resourceFromDb -> updateResources(resourceFromDb, resource));
        } else {
            FormResource formResource = new FormResource(resource.resId, resource.version, form);
            formResource.state = State.OUTDATED;
            resourceDao.create(formResource);
        }
    }

    private void updateResources(FormResource resourceFromDb, FormResource resource) {
        if (resourceFromDb.resId.equals(resource.resId) && !resourceFromDb.version.equals(resource.version)) {
            resourceFromDb.state = State.OUTDATED;
            resourceDao.update(resourceFromDb);
        }
    }

    private void loadOutdatedResources(Form form) {
        List<FormResource> outdatedResources = Stream.of(resourceDao.read(form))
                .filter(resource -> resource.state.equals(State.OUTDATED))
                .collect(Collectors.toList());
        Stream.of(outdatedResources).forEach(resource -> {
            if (resource.resId.contains(".js")) {
                ResourceRequest body = new ResourceRequest(form.formId, resource.resId, resource.version);
                filesController.saveToFile(body, form, resource);
                resource.state = State.UPDATED;
                resourceDao.update(resource);
            } else if (resource.resId.contains(".png") || resource.resId.contains(".jpg")) {
                if (resource.resId.contains("question") || resource.resId.contains("avatar")) {
                    filesController.saveFormattedFile(filesController.formatRequestUrl(resource, form),
                            form, resource);
                    resource.state = State.UPDATED;
                    resourceDao.update(resource);
                }
            } else {
                Log.e(TAG, "WRONG TYPE " + resource.resId);
                resource.state = State.UPDATED;
                resourceDao.update(resource);
            }
        });
    }

    private boolean containsId(FormResource formResource, List<FormResource> formResources) {
        return Stream.of(formResources).filter(resource -> resource.resId.equals(formResource.resId)).count() > 0;
    }
}
