package com.questbase.app.controller.testsession;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.dao.testsession.TestSessionDao;
import com.questbase.app.domain.Form;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.body.TestSessionRequestDto;
import com.questbase.app.utils.RespoSchedulers;

import java.util.Collections;
import java.util.List;

public class DefaultTestSessionController implements TestSessionController {
    private static final String SUCCESS_MESSAGE = "OK";
    private static final String INIT_STATE = "{\"answers\":{},\"pastQuestions\":[]}";
    private final TestSessionDao testSessionDao;
    private final RestApi restApi;
    private TestSession currentTestSession;

    public DefaultTestSessionController(TestSessionDao testSessionDao, RestApi restApi) {
        this.testSessionDao = testSessionDao;
        this.restApi = restApi;
    }

    @Override
    public void setTestSessionState(String state) {
        currentTestSession.state = state;
        testSessionDao.updateByFormIdAndVersion(currentTestSession);
    }

    @Override
    public void completeTestSession(boolean complete) {
        currentTestSession.completed = complete;
        testSessionDao.updateByFormIdAndVersion(currentTestSession);
    }

    @Override
    public void sync() {
        TestSessionRequestDto testSessionsRequest =
                new TestSessionRequestDto(toTestSessionRequests(testSessionDao.readCompletedNotSentTestSessions()
                        .orElse(Collections.emptyList())));
        restApi.postTestSession(testSessionsRequest).subscribeOn(RespoSchedulers.io())
                .subscribe(response -> Stream.of(response.getResults())
                        .filter(item -> item.getState().equals(SUCCESS_MESSAGE))
                        .forEach(item -> testSessionDao.updateSentStatus(item.getLocalId(), true)));
    }

    @Override
    public TestSession getCurrentTestSession(long formId, long formVersion) {
        currentTestSession = testSessionDao.lookup(new Form.Builder(formId).version(formVersion).build())
                .orElseGet(() -> {
                    currentTestSession = new TestSession(formId, formVersion, INIT_STATE, false, false);
                    testSessionDao.create(currentTestSession);
                    return currentTestSession;
                });
        return currentTestSession;
    }

    private List<TestSessionRequestDto.TestSessionRequest> toTestSessionRequests(List<TestSession> list) {
        return Stream.of(list).map(TestSession::toTestSessionRequest).collect(Collectors.toList());
    }
}
