package com.questbase.app.controller.profile;

import com.annimon.stream.Optional;
import com.questbase.app.controller.Event;
import com.questbase.app.controller.Syncable;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.net.body.PayoutRequest;

import java.util.List;

import rx.Observable;

public interface ProfileController extends Syncable {

    Optional<ProfileResponse> getProfile();

    Optional<List<Form>> getProfileForms();

    Observable<Event<ProfileResponse>> observe();

    void setProfile(ProfileResponse profileResponse);

    void performPayout(PayoutRequest payoutRequest);
}