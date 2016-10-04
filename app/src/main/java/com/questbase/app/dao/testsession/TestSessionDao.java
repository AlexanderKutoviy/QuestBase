package com.questbase.app.dao.testsession;

import com.annimon.stream.Optional;
import com.questbase.app.controller.testsession.TestSession;
import com.questbase.app.domain.Form;

import java.util.List;

public interface TestSessionDao {

    /**
     * Create new test session in data base
     *
     * @param testSession test session, that create
     */
    void create(TestSession testSession);

    Optional<TestSession> read(TestSession testSession);

    Optional<List<TestSession>> readCompletedNotSentTestSessions();

    /**
     * Select test session with given formId and NOT completed
     *
     * @param form Form, which doesn't completed
     * @return Test session for input form which doesn't completed
     */
    Optional<TestSession> lookup(Form form);

    void updateByFormIdAndVersion(TestSession testSession);

    void updateSentStatus(int id, boolean sent);

    void delete(TestSession testSession);
}
