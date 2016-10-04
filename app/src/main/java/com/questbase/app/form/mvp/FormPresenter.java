package com.questbase.app.form.mvp;

import android.widget.RelativeLayout;

import com.google.gson.JsonElement;
import com.questbase.app.controller.font.FontController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.controller.testsession.TestSessionWorkflowController;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.flowui.Router;
import com.questbase.app.form.Event;
import com.questbase.app.obsolete.ScriptManager;
import com.questbase.app.personalresult.PersonalResultScreen;

import rx.Observable;

public class FormPresenter {
    private FormController formController;
    private FontController fontController;
    private TestSessionWorkflowController sessionController;
    private TestSessionController testSessionController;
    private FormView view;
    private Router router;
    private long formId;
    private long formVersion;

    public FormPresenter(FormController formController, FontController fontController, TestSessionController testSessionController) {
        this.formController = formController;
        this.fontController = fontController;
        this.testSessionController = testSessionController;
    }

    void onNextView() {
        Observable<Event> eventObservable = sessionController.observe();
        eventObservable.subscribe(this::handleEvent);
        sessionController.startTestSession(formId, formVersion);
    }

    private void handleEvent(Event event) {
        if (event.type.equals(Event.Type.VISIBLE)) {
            RelativeLayout currentView;
            if (view != null) {
                currentView = (RelativeLayout) view.makeViewFromJson(event.item);
                view.setCurrentViewInSwiper(currentView);
            }
        } else if (event.type.equals(Event.Type.PREPARED)) {
            RelativeLayout nextView;
            if (view != null) {
                nextView = (RelativeLayout) view.makeViewFromJson(event.item);
                view.setNextViewInSwiper(nextView);
            }
        }
    }

    void attachView(FormView view, Router router, long formId, long formVersion, ScriptManager.Model model) {
        this.view = view;
        this.view.setMyriadProRegularFont(fontController.getMyriadProRegular());
        this.router = router;
        this.formId = formId;
        this.formVersion = formVersion;
        sessionController = formController.getTestSessionController(testSessionController);
        sessionController.setModel(model);
    }

    void detachView() {
        view = null;
        router = null;
    }

    void onPersonalResultButtonClick() {
        router.goTo(new PersonalResultScreen(formId));
    }

    void moveToNextItem() {
        sessionController.moveToNextItem();
    }

    void onResponse(String questionId, JsonElement response) {
        sessionController.setCurrentResponse(questionId, response);
    }

    void setupHeader(String formTitle) {
        view.showTitle(formTitle);
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(FormPresenter formPresenter);
    }
}
