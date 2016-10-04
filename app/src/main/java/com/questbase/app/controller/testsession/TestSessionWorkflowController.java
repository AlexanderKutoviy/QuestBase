package com.questbase.app.controller.testsession;

import com.google.gson.JsonElement;
import com.questbase.app.form.Event;
import com.questbase.app.obsolete.ScriptManager;

import rx.Observable;

public interface TestSessionWorkflowController {

    void setModel(ScriptManager.Model model);

    /**
     * @param questionId question id, in which setting response
     * @param response   json object value of the response to set
     */
    void setCurrentResponse(String questionId, JsonElement response);

    /**
     * call when moving to next question
     */
    void moveToNextItem();

    /**
     * Initializes test session, launches observable to emit events
     */
    void startTestSession(long formId, long formVersion);

    /**
     * @return observable of {@link Event} objects being emitted on next question events
     */
    Observable<Event> observe();

}