package com.questbase.app.controller.form;

import android.annotation.SuppressLint;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.resource.ResourceController;
import com.questbase.app.controller.testsession.RhinoTestSessionWorkflowController;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.controller.testsession.TestSessionWorkflowController;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.State;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.Category;
import com.questbase.app.net.entity.VersionedForm;
import com.questbase.app.sync.SyncUtils;

import java.util.List;

public class DefaultFormController implements FormController {

    private final RestApi restApi;
    private final QuestDao questDao;
    private final ResourceController resourceController;
    private final FilesController filesController;

    public DefaultFormController(RestApi restApi,
                                 QuestDao questDao,
                                 ResourceController resourceController,
                                 FilesController filesController) {
        this.restApi = restApi;
        this.questDao = questDao;
        this.resourceController = resourceController;
        this.filesController = filesController;
    }

    @Override
    public void sync() {
        Log.d(SyncUtils.SYNC_TAG, "Forms sync");
        loadDescriptorsForOutdatedForms();
    }

    @Override
    public void sync(Category category) {
        List<VersionedForm> versionedForms = restApi.getVersionedForms(category).toBlocking().first();
        List<Form> forms = questDao.readAll();
        Stream.of(versionedForms).forEach(version -> performFormDatabaseSync(version, forms, category));
    }

    @Override
    public List<Form> getAllUpdatedForms() {
        return Stream.of(questDao.readAll())
                .filter(form -> form.state.equals(State.UPDATED))
                .filter(updatedForms -> filesController.hasResources(updatedForms.formId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Form> getFormsByCategory(Category category) {
        return questDao.read(category);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Optional<Form> getFormById(long formId) {
        if (filesController.hasResources(formId)) {
            return questDao.read(new Form.Builder(formId).build());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public TestSessionWorkflowController getTestSessionController(TestSessionController testSessionController) {
        return new RhinoTestSessionWorkflowController(testSessionController);
    }

    private void loadDescriptorsForOutdatedForms() {
        Stream.of(questDao.readAll())
                .filter(form -> form.state.equals(State.OUTDATED))
                .forEach(outdated -> {
                    Form formFromServer = restApi.getFormDescriptor(outdated).toBlocking().first();
                    resourceController.sync(formFromServer);
                    setFormUpdated(formFromServer);
                });
    }

    private void performFormDatabaseSync(VersionedForm versionedForm, List<Form> forms, Category category) {
        if (containsId(versionedForm, forms)) {
            Stream.of(forms).forEach(form -> updateForm(versionedForm, form, category));
        } else {
            Form form = new Form.Builder(versionedForm.formId).category(category).state(State.OUTDATED).build();
            questDao.create(form);
        }
    }

    private boolean containsId(VersionedForm versionedForm, List<Form> forms) {
        return Stream.of(forms).filter(form -> form.formId == versionedForm.formId).count() > 0;
    }

    private void updateForm(VersionedForm versionedForm, Form form, Category category) {
        if (versionedForm.formId == form.formId && versionedForm.version != form.version) {
            form.state = State.OUTDATED;
            form.category = category;
            questDao.update(form, category);
        }
    }

    private void setFormUpdated(Form form) {
        form.state = State.UPDATED;
        questDao.update(form);
    }

    private List<Form> getUpdatedFormsByCategory(Category category) {
        return Stream.of(questDao.read(category))
                .filter(form -> form.state.equals(State.UPDATED))
                .collect(Collectors.toList());
    }
}