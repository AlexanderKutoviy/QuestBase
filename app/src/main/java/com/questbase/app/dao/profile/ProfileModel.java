package com.questbase.app.dao.profile;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.dao.profile.email.UserEmailModel;
import com.questbase.app.dao.profile.email.UserEmailModel_Table;
import com.questbase.app.dao.profile.phone.UserPhoneModel;
import com.questbase.app.dao.profile.phone.UserPhoneModel_Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.domain.ProfileResponse;

import java.util.List;

@Table(database = RespoDatabase.class)
public class ProfileModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public String userId;

    @Column
    public String login;

    @Column
    public int debugRole;

    @Column
    public String avatarUrl;

    @Column
    public String name;

    @Column
    public String currentResearchFormsCount;

    @Column
    public String researchFormsAvg;

    @Column
    public String currentFunFormsCount;

    @Column
    public String funFormsAvg;

    @Column
    public float balance;

    @Column
    public int processFormCount;

    @Column
    public long lastTransactionTime;

    @Column
    public String transactionsCount;

    @Column
    public int charityTransactionsCount;

    public List<UserPhoneModel> phones;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "phones")
    public List<UserPhoneModel> getPhones() {
        if (phones == null || phones.isEmpty()) {
            phones = SQLite.select()
                    .from(UserPhoneModel.class)
                    .where(UserPhoneModel_Table.userId.eq(userId))
                    .queryList();
        }
        return phones;
    }

    public List<UserEmailModel> emails;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "emails")
    public List<UserEmailModel> getEmails() {
        if (emails == null || emails.isEmpty()) {
            emails = SQLite.select()
                    .from(UserEmailModel.class)
                    .where(UserEmailModel_Table.userId.eq(userId))
                    .queryList();
        }
        return emails;
    }

    public ProfileModel(ProfileResponse profileResponse) {
        this.userId = profileResponse.userId;
        this.login = profileResponse.login;
        this.debugRole = profileResponse.debugRole;
        this.avatarUrl = profileResponse.avatarUrl;
        this.currentResearchFormsCount = profileResponse.currentResearchFormsCount;
        this.researchFormsAvg = profileResponse.researchFormsAvg;
        this.currentFunFormsCount = profileResponse.currentFunFormsCount;
        this.funFormsAvg = profileResponse.funFormsAvg;
        this.balance = profileResponse.balance;
        this.processFormCount = profileResponse.processFormCount;
        this.lastTransactionTime = profileResponse.lastTransactionTime.getTime();
        this.transactionsCount = profileResponse.transactionsCount;
        this.charityTransactionsCount = profileResponse.charityTransactionsCount;
    }

    public ProfileModel() {
    }

    public ProfileResponse toProfile() {
        return new ProfileResponse.Builder(login, userId)
                .debugRole(debugRole)
                .avatar(avatarUrl)
                .currentResearchFormsCount(currentResearchFormsCount)
                .researchFormsAvg(researchFormsAvg)
                .currentFunFormsCount(currentFunFormsCount)
                .phone(Stream.of(phones)
                        .map(UserPhoneModel::toUserPhoneDto)
                        .collect(Collectors.toList()))
                .email(Stream.of(emails)
                        .map(UserEmailModel::toUserEmailDto)
                        .collect(Collectors.toList()))
                .funFormsAvg(funFormsAvg)
                .balance(balance)
                .processFormCount(processFormCount)
                .lastTransactionTime(lastTransactionTime)
                .transactionsCount(transactionsCount)
                .charityTransactionsCount(charityTransactionsCount)
                .build();
    }
}
