package com.questbase.app.controller.profile;

import android.util.Log;

import com.annimon.stream.Optional;
import com.questbase.app.controller.Event;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.profile.ProfileDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.body.PayoutRequest;
import com.questbase.app.utils.PrefsHelper;
import com.questbase.app.utils.RespoSchedulers;

import java.util.List;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class DefaultProfileController implements ProfileController {

    private static final String TAG = DefaultProfileController.class.getSimpleName();
    private final RestApi restApi;
    private final ProfileDao profileDao;
    private final AuthDao authDao;
    private final FilesController filesController;
    private final PrefsHelper prefsHelper;
    private ReplaySubject<Event<ProfileResponse>> replaySubject;

    public DefaultProfileController(FilesController filesController, ProfileDao profileDao,
                                    AuthDao authDao, RestApi restApi, PrefsHelper prefsHelper) {
        this.profileDao = profileDao;
        this.authDao = authDao;
        this.restApi = restApi;
        this.prefsHelper = prefsHelper;
        this.replaySubject = ReplaySubject.create(1);
        this.filesController = filesController;
    }

    @Override
    public Optional<ProfileResponse> getProfile() {
        return authDao.getAuth().flatMap(auth -> {
            replaySubject.onNext(new Event<>(profileDao.getProfile(auth.userId).get(), Event.Type.UPDATE));
            return profileDao.getProfile(auth.userId);
        });
    }

    @Override
    public Optional<List<Form>> getProfileForms() {
        return authDao.getAuth().flatMap(auth -> profileDao.getProfileForms(auth.userId));
    }

    @Override
    public Observable<Event<ProfileResponse>> observe() {
        getProfile();
        return replaySubject;
    }

    @Override
    public void setProfile(ProfileResponse profileResponse) {
        profileDao.setProfile(profileResponse);
        if (profileResponse.debugRole == 1) {
            prefsHelper.setDebugRole(true);
        } else {
            prefsHelper.setDebugRole(false);
        }
        replaySubject.onNext(new Event<>(profileResponse, Event.Type.UPDATE));
    }

    @Override
    public void performPayout(PayoutRequest payoutRequest) {
        restApi.performPayout(payoutRequest).subscribeOn(RespoSchedulers.io())
                .subscribe(response -> Log.d(TAG, "payout response -> " + response.toString()));
    }

    @Override
    public Observable<SyncResult> sync() {
        return authDao.getAuth().map(auth -> restApi.getProfile()
                .map(profile -> {
                    profile.userId = auth.userId;
                    filesController.saveUsersAvatar(profile.avatarUrl);
                    setProfile(profile);
                    return SyncResult.SUCCESS;
                })).orElse(Observable.just(SyncResult.FAIL));
    }
}