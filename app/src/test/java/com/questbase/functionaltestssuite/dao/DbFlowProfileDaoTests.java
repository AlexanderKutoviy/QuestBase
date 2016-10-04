package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.profile.DbFlowProfileDao;
import com.questbase.app.dao.profile.ProfileDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.ProfileResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowProfileDaoTests {

    @Test
    public void testSetMultipleUsers() {
        new FlowConfig.Builder(
                RuntimeEnvironment.application.getApplicationContext()).build();

        final String userId1 = "Євгеній Нечипоренко";
        final String userId2 = "Марія Галушко";
        final String userId3 = "Олександр Кутовий";
        List<Form> forms = new ArrayList<>();
        ProfileResponse profileResponse1 = new ProfileResponse.Builder("Євгеній Нечипоренко", userId1)
                .avatar("/media\\/user\\/41136\\/561003bf_200x200.jpeg")
                .currentFunFormsCount("0")
                .researchFormsAvg("18.9429")
                .currentFunFormsCount("2")
                .funFormsAvg("4.9122")
                .balance(0)
                .processFormCount(0)
                .lastTransactionTime(0)
                .transactionsCount("0")
                .charityTransactionsCount(0)
                .listOfUsersForms(new ArrayList<>())
                .email(new ArrayList<>())
                .phone(new ArrayList<>())
                .build();
        ProfileResponse profileResponse2 = new ProfileResponse.Builder("Марія Галушко", userId2)
                .avatar("/media\\/user\\/41136\\/561003bf_200x200.jpeg")
                .currentFunFormsCount("1")
                .researchFormsAvg("19.429")
                .currentFunFormsCount("3")
                .funFormsAvg("3.9122")
                .balance(0)
                .lastTransactionTime(0)
                .processFormCount(0)
                .transactionsCount("1")
                .charityTransactionsCount(1)
                .email(new ArrayList<>())
                .phone(new ArrayList<>())
                .listOfUsersForms(forms)
                .build();
        ProfileResponse profileResponse3 = new ProfileResponse.Builder("Олександр Кутовий", userId3)
                .avatar("/media\\/user\\/41136\\/561003bf_200x200.jpeg")
                .currentFunFormsCount("2")
                .researchFormsAvg("14.1429")
                .currentFunFormsCount("1")
                .funFormsAvg("2.8522")
                .balance(0)
                .processFormCount(0)
                .transactionsCount("0")
                .email(new ArrayList<>())
                .phone(new ArrayList<>())
                .lastTransactionTime(0)
                .charityTransactionsCount(2)
                .listOfUsersForms(forms)
                .build();

        ProfileDao profileDao = new DbFlowProfileDao();
        profileDao.setProfile(profileResponse1);
        profileDao.setProfile(profileResponse2);
        profileDao.setProfile(profileResponse3);

        assertEquals(profileResponse1, profileDao.getProfile(userId1).get());
        assertEquals(profileResponse2, profileDao.getProfile(userId2).get());
        assertEquals(profileResponse3, profileDao.getProfile(userId3).get());

        FlowManager.destroy();
    }

    @Test
    public void testSetUpdateUsers() {
        new FlowConfig.Builder(
                RuntimeEnvironment.application.getApplicationContext()).build();
        final String userId = "Євгеній Нечипоренко";
        List<Form> forms = new ArrayList<>();

        ProfileResponse profileResponse1 = new ProfileResponse.Builder("Євгеній Нечипоренко", userId)
                .avatar("/media\\/user\\/41136\\/561003bf_200x200.jpeg")
                .currentFunFormsCount("0")
                .researchFormsAvg("25.9429")
                .currentFunFormsCount("2")
                .funFormsAvg("4.9122")
                .balance(0)
                .processFormCount(0)
                .email(new ArrayList<>())
                .phone(new ArrayList<>())
                .lastTransactionTime(0)
                .transactionsCount("0")
                .charityTransactionsCount(3)
                .listOfUsersForms(forms)
                .build();
        ProfileResponse profileResponse2 = new ProfileResponse.Builder("Vasya Vasya", userId)
                .avatar("/media\\/user\\/41136\\/561003bf_200x200.jpeg")
                .currentFunFormsCount("1")
                .researchFormsAvg("21.429")
                .currentFunFormsCount("3")
                .funFormsAvg("4.111")
                .email(new ArrayList<>())
                .phone(new ArrayList<>())
                .lastTransactionTime(0)
                .balance(0)
                .processFormCount(5)
                .transactionsCount("1")
                .charityTransactionsCount(4)
                .listOfUsersForms(forms)
                .build();

        ProfileDao profileDao = new DbFlowProfileDao();
        profileDao.setProfile(profileResponse1);
        assertEquals(profileResponse1, profileDao.getProfile(userId).get());

        profileDao.setProfile(profileResponse2);
        assertEquals(profileResponse2, profileDao.getProfile(userId).get());

        FlowManager.destroy();
    }
}