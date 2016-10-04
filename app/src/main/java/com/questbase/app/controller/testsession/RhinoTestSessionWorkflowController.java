package com.questbase.app.controller.testsession;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.questbase.app.form.Event;
import com.questbase.app.obsolete.ScriptManager;
import com.questbase.app.utils.JsonUtils;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RhinoTestSessionWorkflowController implements TestSessionWorkflowController {
    private final static String FIRST_RESPONSE = "{first:1}";
    private static final String RESULT = "Result";
    private static final String TYPE = "type";
    private ScriptManager.Model model;
    private PublishSubject<Event> publishSubject;
    private JsonElement currentResponse;
    private JsonElement nextQuestion;
    private final TestSessionController testSessionController;


    public RhinoTestSessionWorkflowController(TestSessionController testSessionController) {
        this.testSessionController = testSessionController;
        publishSubject = PublishSubject.create();
    }

    @Override
    public void setModel(ScriptManager.Model model) {
        this.model = model;
    }

    @Override
    public void setCurrentResponse(String questionId, JsonElement response) {
        this.currentResponse = response;
        model.setAnswer(questionId, response).toBlocking().first();
        nextQuestion = model.getNextQuestion().toBlocking().first();
        if (nextQuestion.isJsonNull()) {
            nextQuestion = generatePreResult();
        } else {
            nextQuestion = model.getNextQuestion().toBlocking().first().getAsJsonObject();
        }
        testSessionController.setTestSessionState(model.getState().toBlocking().first());
        Event event = new Event(Event.Type.PREPARED,
                nextQuestion.getAsJsonObject(),
                response,
                model.getProgress().toBlocking().first());
        onNext(event);
    }

    @Override
    public void moveToNextItem() {
        if (nextQuestion.equals(generatePreResult())) {
            testSessionController.completeTestSession(true);
        }
        Event event = new Event(Event.Type.VISIBLE,
                nextQuestion.getAsJsonObject(),
                currentResponse,
                model.getProgress().toBlocking().first());
        if (event.item != null) {
            onNext(event);
        }
    }

    @Override
    public void startTestSession(long formId, long formVersion) {
        JsonObject response = JsonUtils.toJsonObject(FIRST_RESPONSE);
        model.setState(testSessionController
                .getCurrentTestSession(formId, formVersion).state).toBlocking().first();
        Event event = new Event(Event.Type.VISIBLE,
                model.getNextQuestion().toBlocking().first().getAsJsonObject(),
                response, model.getProgress().toBlocking().first());
        onNext(event);
    }

    @Override
    public Observable<Event> observe() {
        return publishSubject;
    }

    private void onNext(Event event) {
        publishSubject.onNext(event);
    }

    private JsonObject generatePreResult() {
        JsonObject preResult = new JsonObject();
        preResult.add(TYPE, new JsonPrimitive(RESULT));
        return preResult;
    }
}
