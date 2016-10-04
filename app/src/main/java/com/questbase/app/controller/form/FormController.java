package com.questbase.app.controller.form;

import com.annimon.stream.Optional;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.controller.testsession.TestSessionWorkflowController;
import com.questbase.app.domain.Form;
import com.questbase.app.net.entity.Category;

import java.util.List;

public interface FormController {
    /**
     * Called via SyncAdapter to synchronize Form descriptors
     */
    void sync();

    /**
     * Called by CategoryController to synchronize forms for current category
     */
    void sync(Category category);

    List<Form> getAllUpdatedForms();

    List<Form> getFormsByCategory(Category category);

    Optional<Form> getFormById(long formId);

    TestSessionWorkflowController getTestSessionController(TestSessionController testSessionController);
}