package com.questbase.functionaltestssuite.dao;

import com.annimon.stream.Optional;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.testsession.TestSession;
import com.questbase.app.dao.testsession.DbFlowTestSessionDao;
import com.questbase.app.dao.testsession.TestSessionDao;
import com.questbase.app.domain.Form;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowTestSessionDaoTests {

    @Test
    public void testSetAndReadTestSessions() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSessionDao testSessionDao = new DbFlowTestSessionDao();
        assertEquals(Collections.emptyList(), testSessionDao.readCompletedNotSentTestSessions().get());
        for (TestSession testSession : getTestSessions()) {
            testSessionDao.create(testSession);
        }
        assertEquals(getTestSessions(), testSessionDao.readCompletedNotSentTestSessions().get());
        FlowManager.destroy();
    }

    @Test
    public void testReadOneTestSession() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSessionDao testSessionDao = new DbFlowTestSessionDao();
        assertEquals(Collections.emptyList(), testSessionDao.readCompletedNotSentTestSessions().get());
        TestSession testSession = new TestSession(1, (long) 1, (long) 1, "{}", false, false);
        assertNotEquals(Optional.of(testSession), testSessionDao.read(testSession));
        testSessionDao.create(testSession);
        assertEquals(Optional.of(testSession), testSessionDao.read(testSession));
        assertNotEquals(Optional.of(testSession), testSessionDao.readCompletedNotSentTestSessions());
        FlowManager.destroy();
    }

    @Test
    public void testDeleteTestSession() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSessionDao testSessionDao = new DbFlowTestSessionDao();
        assertEquals(Collections.emptyList(), testSessionDao.readCompletedNotSentTestSessions().get());
        TestSession testSession = new TestSession(1, (long) 1, (long) 1, "{}", false, false);
        assertNotEquals(Optional.of(testSession), testSessionDao.read(testSession));
        testSessionDao.create(testSession);
        assertEquals(Optional.of(testSession), testSessionDao.read(testSession));
        assertNotEquals(Optional.of(testSession), testSessionDao.readCompletedNotSentTestSessions());
        testSession.completed = true;
        testSessionDao.updateByFormIdAndVersion(testSession);
        assertSame(1, testSessionDao.readCompletedNotSentTestSessions().get().size());
        testSessionDao.delete(testSession);
        assertEquals(Collections.emptyList(), testSessionDao.readCompletedNotSentTestSessions().get());
        FlowManager.destroy();
    }

    @Test
    public void testUpdateTestSessions() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSessionDao testSessionDao = new DbFlowTestSessionDao();
        TestSession testSession = new TestSession(1, (long) 1, (long) 1, "{}", false, false);
        testSessionDao.create(testSession);
        for (TestSession currentTestSession : getTestSessions()) {
            testSessionDao.create(currentTestSession);
        }
        assertEquals(Optional.of(testSession), testSessionDao.read(testSession));
        testSession.completed = true;
        testSession.sent = true;
        testSessionDao.updateSentStatus(1, true);
        assertTrue(testSessionDao.read(testSession).get().sent);

        testSession.state = "{answers:1,2}";
        testSessionDao.updateByFormIdAndVersion(testSession);
        assertEquals(testSession.state, testSessionDao.read(testSession).get().state);
        assertEquals(testSession, testSessionDao.read(testSession).get());
        testSessionDao.delete(testSession);

        for (TestSession currentTestSession : getTestSessions()) {
            currentTestSession.completed = false;
            testSessionDao.updateByFormIdAndVersion(currentTestSession);
        }
        assertEquals(Collections.emptyList(), testSessionDao.readCompletedNotSentTestSessions().get());
        FlowManager.destroy();
    }

    @Test
    public void testLookUpTestSession() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        Form form = new Form.Builder(1L).version(1L).build();
        TestSessionDao testSessionDao = new DbFlowTestSessionDao();
        TestSession testSession = new TestSession(1, form.formId, form.version, "{}", false, false);
        testSessionDao.create(testSession);
        for (TestSession currentTestSession : getTestSessions()) {
            testSessionDao.create(currentTestSession);
        }
        assertEquals(Optional.of(testSession), testSessionDao.read(testSession));
        assertEquals(testSession, testSessionDao.lookup(form).get());
        testSession.completed = true;
        testSessionDao.updateByFormIdAndVersion(testSession);
        assertNotEquals(Optional.of(testSession), testSessionDao.lookup(form));
        assertEquals(Optional.empty(), testSessionDao.lookup(form));
        FlowManager.destroy();
    }

    private List<TestSession> getTestSessions() {
        List<TestSession> testSessions = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            testSessions.add(new TestSession(i, (long) i, (long) i, "{}", true, false));
        }
        return testSessions;
    }
}
