package com.questbase.app.controller.testsession;

public interface TestSessionController {

    void setTestSessionState(String state);

    void completeTestSession(boolean complete);

    void sync();

    TestSession getCurrentTestSession(long formId, long formVersion);
}
