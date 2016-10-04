package com.questbase.functionaltestssuite.controller;

import com.annimon.stream.Optional;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.testsession.DefaultTestSessionController;
import com.questbase.app.controller.testsession.TestSession;
import com.questbase.app.controller.testsession.TestSessionController;
import com.questbase.app.dao.testsession.DbFlowTestSessionDao;
import com.questbase.app.dao.testsession.TestSessionDao;
import com.questbase.app.net.MockRestApi;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.body.TestSessionRequestDto;
import com.questbase.app.net.objects.TestSessionResponseDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class TestSessionControllerTests {

    @Test
    public void testPassingQuestionnaire() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSessionController defaultTestSessionController = generateTestSessionController();
        long formId = (long) 1;
        long formVersion = (long) 1;
        TestSession currentTestSession = defaultTestSessionController.getCurrentTestSession(formId, formVersion);
        currentTestSession.id = 1;
        assertEquals(currentTestSession, defaultTestSessionController.getCurrentTestSession(formId, formVersion));
        assertEquals(currentTestSession, defaultTestSessionController.getCurrentTestSession(formId, formVersion));

        defaultTestSessionController.completeTestSession(true);
        currentTestSession.completed = true;
        assertNotEquals(currentTestSession, defaultTestSessionController.getCurrentTestSession(formId, formVersion));

        defaultTestSessionController.setTestSessionState("{\"answers\":{1,2},\"pastQuestions\":[1]}");
        currentTestSession.completed = false;
        currentTestSession.state = "{\"answers\":{1,2},\"pastQuestions\":[1]}";
        assertEquals(currentTestSession, defaultTestSessionController.getCurrentTestSession(formId, formVersion));

        currentTestSession.completed = false;
        assertEquals(currentTestSession, defaultTestSessionController.getCurrentTestSession(formId, formVersion));
        FlowManager.destroy();
    }

    @Test
    public void syncTest() {
        RestApi restApi = mock(RestApi.class);
        TestSessionDao testSessionDao = mock(TestSessionDao.class);
        TestSessionController testSessionController = new DefaultTestSessionController(testSessionDao, restApi);
        Observable<TestSessionResponseDto> testSessionResponseDtoObservable = PublishSubject.create();
        when(restApi.postTestSession(any(TestSessionRequestDto.class))).thenReturn(testSessionResponseDtoObservable);
        ArrayList<TestSession> testSessions = new ArrayList<>();
        testSessions.add(new TestSession(1, (long) 1, (long) 1, "{}", true, false));
        testSessions.add(new TestSession(2, (long) 2, (long) 1, "{}", true, false));
        testSessions.add(new TestSession(3, (long) 3, (long) 1, "{}", true, false));
        when(testSessionDao.readCompletedNotSentTestSessions()).thenReturn(Optional.of(testSessions));

        testSessionController.sync();
        verify(testSessionDao, times(1)).readCompletedNotSentTestSessions();
        verify(restApi, times(1)).postTestSession(any(TestSessionRequestDto.class));
        testSessionResponseDtoObservable.subscribe(response -> verify(testSessionDao, times(1)).updateSentStatus(any(int.class), any(boolean.class)));
    }

    public TestSessionController generateTestSessionController() {
        TestSessionDao testSessionDao = new DbFlowTestSessionDao();
        RestApi restApi = new MockRestApi();
        return new DefaultTestSessionController(testSessionDao, restApi);
    }
}
