package com.questbase.app.controller.testsession;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.questbase.app.form.Event;
import com.questbase.app.obsolete.ScriptManager;
import com.questbase.app.utils.JsonUtils;
import com.questbase.app.utils.Objects;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.subjects.PublishSubject;

public class MockTestSessionWorkflowController implements TestSessionWorkflowController {

    private final Map<String, Object> responses = new HashMap<>();
    private Event lastEvent;
    private Map<JsonObjectWrapper, JsonObject> nextQuestionMap = new HashMap<>();
    private JsonObject firstQuestion;
    private String currentQuestionId;
    private PublishSubject<Event> publishSubject;

    public MockTestSessionWorkflowController(JsonObject firstQuestion, Map<JsonObject, JsonObject> jsonQuestionMap) {
        this.nextQuestionMap = Stream.of(jsonQuestionMap.keySet()).collect(
                Collectors.toMap(JsonObjectWrapper::new, jsonQuestionMap::get));
        this.firstQuestion = firstQuestion;
        this.publishSubject = PublishSubject.create();
    }

    @Override
    public void moveToNextItem() {
        Event event = generateVisibleEvent();
        if (event.item != null) {
            onNext(new Event(event));
        }
    }

    @Override
    public void startTestSession(long formId, long formVersion) {
        onNext(new Event(generateFirstEvent()));
    }

    @Override
    public void setModel(ScriptManager.Model model) {

    }

    @Override
    public void setCurrentResponse(String questionId, JsonElement response) {
        currentQuestionId = response.getAsJsonObject().entrySet().iterator().next().getKey();
        Object currentAnswer = response.getAsJsonObject().get(currentQuestionId);
        responses.put(currentQuestionId, currentAnswer);
        JsonObject nextQuestion = getQuestion("{" + currentQuestionId + ":" + currentAnswer + "}");
        if (Objects.equal(nextQuestion, null)) {
            nextQuestion = getQuestion("{" + currentQuestionId + ":null}");
        }
        Event event = new Event(Event.Type.PREPARED, nextQuestion, response, 1);
        onNext(new Event(event));
    }

    @Override
    public Observable<Event> observe() {
        return publishSubject;
    }

    private void onNext(Event event) {
        lastEvent = event;
        if (event.item != null) {
            publishSubject.onNext(event);
        }
    }

    private Event generateVisibleEvent() {
        Event event = lastEvent;
        event.type = Event.Type.VISIBLE;
        return event;
    }

    private Event generateFirstEvent() {
        return new Event(Event.Type.VISIBLE,
                firstQuestion,
                JsonUtils.toJsonObject("{string:2123}"), 1);
    }

    private JsonObject getQuestion(String answer) {
        return nextQuestionMap.get(new JsonObjectWrapper(JsonUtils.toJsonObject(answer)));
    }

    private static class JsonObjectWrapper {
        private final JsonObject object;

        JsonObjectWrapper(JsonObject object) {
            this.object = object;
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof JsonObjectWrapper && Objects.equal(this.object, ((JsonObjectWrapper) object).object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.object);
        }
    }
}