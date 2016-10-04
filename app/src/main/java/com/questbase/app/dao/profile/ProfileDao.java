package com.questbase.app.dao.profile;

import com.annimon.stream.Optional;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.ProfileResponse;

import java.util.List;

public interface ProfileDao {

    void setProfile(ProfileResponse profileResponse);

    Optional<ProfileResponse> getProfile(String userId);

    Optional<List<Form>> getProfileForms(String userId);
}