package com.questbase.app.controller.testsession;

import com.questbase.app.net.body.TestSessionRequestDto;
import com.questbase.app.utils.Objects;

public class TestSession {

    public int id;
    public long formId;
    public long formVersion;
    public boolean completed;
    public boolean sent;

    /**
     * Rhino js state
     */
    public String state;

    public TestSession(Long formId, Long formVersion, String state, boolean completed, boolean sent) {
        this.formId = formId;
        this.formVersion = formVersion;
        this.state = state;
        this.completed = completed;
        this.sent = sent;
    }

    public TestSession(int id, long formId, long formVersion, String state, boolean completed, boolean sent) {
        this.id = id;
        this.formId = formId;
        this.formVersion = formVersion;
        this.state = state;
        this.completed = completed;
        this.sent = sent;
    }

    TestSessionRequestDto.TestSessionRequest toTestSessionRequest() {
        return new TestSessionRequestDto.TestSessionRequest(id, formId, formVersion, state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, formId, formVersion, state, completed, sent);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof TestSession)) {
            return false;
        }
        TestSession testSession = (TestSession) object;
        return Objects.equal(id, testSession.id)
                && Objects.equal(formId, testSession.formId)
                && Objects.equal(formVersion, testSession.formVersion)
                && Objects.equal(state, testSession.state)
                && Objects.equal(completed, testSession.completed)
                && Objects.equal(sent, testSession.sent);
    }

    @Override
    public String toString() {
        return "\nTestSession{" +
                "  id=" + id +
                ", formId=" + formId +
                ", formVersion=" + formVersion +
                ", completed=" + completed +
                ", sent=" + sent +
                ", state='" + state + '\'' +
                '}';
    }
}
