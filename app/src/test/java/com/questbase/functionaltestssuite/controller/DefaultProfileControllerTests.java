package com.questbase.functionaltestssuite.controller;

import com.annimon.stream.Optional;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.Event;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.profile.DefaultProfileController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.form.DbFlowQuestDao;
import com.questbase.app.dao.form.QuestDao;
import com.questbase.app.dao.profile.DbFlowProfileDao;
import com.questbase.app.dao.profile.ProfileDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.domain.State;
import com.questbase.app.net.RestApi;
import com.questbase.app.utils.Auth;
import com.questbase.app.utils.PrefsHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DefaultProfileControllerTests {

    @Test
    public void testSetUpdateGetScenario() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TestSubscriber<Event<ProfileResponse>> testSubscriber = new TestSubscriber<>();
        String user1 = "Евгений Нечипоренко";
        List<Form> forms = initForms();
        int eventsCounter = 0;
        ProfileDao profileDao = new DbFlowProfileDao();
        AuthDao authDao = mock(AuthDao.class);
        RestApi restApi = mock(RestApi.class);
        FilesController filesController = mock(FilesController.class);
        PrefsHelper prefsHelper = mock(PrefsHelper.class);
        when(authDao.getAuth()).thenReturn(Optional.of(new Auth(user1, null, null)));
        DefaultProfileController defaultProfileController = new DefaultProfileController(
                filesController, profileDao, authDao, restApi, prefsHelper);

        ProfileResponse profileResponse1 = new ProfileResponse.Builder("Евгений Нечипоренко", "1")
                .avatar("/media\\/user\\/41136\\/561003bf_200x200.jpeg")
                .currentFunFormsCount("0")
                .researchFormsAvg("18.9429")
                .currentFunFormsCount("2")
                .funFormsAvg("4.9122")
                .balance(0)
                .processFormCount(0)
                .transactionsCount("0")
                .lastTransactionTime(0)
                .listOfUsersForms(forms)
                .email(new ArrayList<>())
                .phone(new ArrayList<>())
                .charityTransactionsCount(0)
                .build();

        ProfileResponse profileResponse2 = new ProfileResponse.Builder("Евгений Нечипоренко", "1")
                .avatar("/media\\/user\\/41136\\/561003bf_200x200.jpeg")
                .currentFunFormsCount("1")
                .researchFormsAvg("19.429")
                .currentFunFormsCount("3")
                .funFormsAvg("3.9122")
                .balance(0)
                .email(new ArrayList<>())
                .phone(new ArrayList<>())
                .lastTransactionTime(0)
                .processFormCount(0)
                .transactionsCount("1")
                .listOfUsersForms(forms)
                .charityTransactionsCount(1)
                .build();

        defaultProfileController.observe().subscribe(testSubscriber);
        List<Event<ProfileResponse>> list = testSubscriber.getOnNextEvents();

        defaultProfileController.setProfile(profileResponse1);
        eventsCounter++;
        defaultProfileController.setProfile(profileResponse2);
        eventsCounter++;

        testSubscriber.assertValueCount(eventsCounter);
        assertEquals(Event.Type.UPDATE, list.get(0).type);
        assertEquals(Event.Type.UPDATE, list.get(1).type);
        assertEquals(profileResponse1, list.get(0).data);
        assertEquals(profileResponse2, list.get(1).data);

        FlowManager.destroy();
    }

    private List<Form> initForms() {
        QuestDao questDao = new DbFlowQuestDao();
        List<Form> forms = new ArrayList<>();
        forms.add(new Form.Builder(11).title("form 1").libVersion("afasf").state(State.UPDATED).version(123).build());
        forms.add(new Form.Builder(21).title("form 2").libVersion("fdgfdg").state(State.UPDATED).version(214).build());
        forms.add(new Form.Builder(45).title("form 3 ").libVersion("afasdfgdgf").state(State.UPDATED).version(321).build());

        questDao.create(forms.get(0));
        questDao.create(forms.get(1));
        questDao.create(forms.get(2));
        return forms;
    }
}
