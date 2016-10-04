package com.questbase.app.dao.profile;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.dao.form.FormModel;
import com.questbase.app.dao.form.FormModel_Table;
import com.questbase.app.dao.form.ProfileFormModel;
import com.questbase.app.dao.form.ProfileFormModel_Table;
import com.questbase.app.dao.profile.email.UserEmailModel;
import com.questbase.app.dao.profile.phone.UserPhoneModel;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.domain.State;
import com.questbase.app.domain.UserEmailDto;
import com.questbase.app.domain.UserPhoneDto;

import java.util.List;

public class DbFlowProfileDao implements ProfileDao {

    @Override
    public void setProfile(ProfileResponse profileResponse) {
        new ProfileModel(profileResponse).saveOnDuplicateUpdate();
        saveEmails(profileResponse.emails, profileResponse.userId);
        savePhones(profileResponse.phones, profileResponse.userId);
        saveForms(profileResponse);
    }

    @Override
    public Optional<ProfileResponse> getProfile(String userId) {
        // map and optional is still required for cases when selected list is empty
        Optional<ProfileResponse> profileResponse = Optional.of(SQLite.select()
                .from(ProfileModel.class)
                .where(ProfileModel_Table.userId.eq(userId)).querySingle().toProfile());
        List<ProfileFormModel> profileFormModels = SQLite.select().from(ProfileFormModel.class).where(
                ProfileFormModel_Table.userId.eq(userId)).queryList();
        profileResponse.get().listOfUsersForms = Stream.of(profileFormModels)
                .filter(model -> model.userId.equals(userId))
                .map(this::getUpdatedForms)
                .filter(userForm -> userForm != null)
                .collect(Collectors.toList());
        return profileResponse;
    }

    @Override
    public Optional<List<Form>> getProfileForms(String userId) {
        List<ProfileFormModel> profileFormModels = SQLite.select().from(ProfileFormModel.class).where(
                ProfileFormModel_Table.userId.eq(userId)).queryList();
        return Optional.of(Stream.of(profileFormModels)
                .filter(model -> model.userId.equals(userId))
                .map(this::getUpdatedForms)
                .filter(userForm -> userForm != null)
                .collect(Collectors.toList()));
    }

    private void saveForms(ProfileResponse profile) {
        ProfileFormModel profileFormModel = new ProfileFormModel();
        Stream.of(profile.listOfUsersForms).forEach(form -> {
            profileFormModel.userId = profile.userId;
            profileFormModel.formId = form.formId;
            profileFormModel.save();
        });
    }

    private Form getUpdatedForms(ProfileFormModel profileFormModel) {
        String STATE = State.UPDATED.name();
        FormModel formModel = (SQLite.select()
                .from(FormModel.class)
                .where(FormModel_Table.id.eq(profileFormModel.formId))
                .and(FormModel_Table.state.eq(STATE))
                .querySingle());
        if (formModel != null) {
            return formModel.toFormResponseWithResource();
        } else {
            return null;
        }
    }

    private void saveEmails(List<UserEmailDto> emails, String userId) {
        Stream.of(emails).forEach(email -> new UserEmailModel(email, userId).saveOnDuplicateUpdate());
    }

    private void savePhones(List<UserPhoneDto> phones, String userId) {
        Stream.of(phones).forEach(phone -> new UserPhoneModel(phone, userId).saveOnDuplicateUpdate());
    }
}
