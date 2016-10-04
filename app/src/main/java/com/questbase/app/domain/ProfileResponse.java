package com.questbase.app.domain;

import com.questbase.app.utils.Objects;

import java.util.Date;
import java.util.List;

public class ProfileResponse {

    public String userId;
    public String login;
    public int debugRole;
    public String avatarUrl;
    public String currentResearchFormsCount;
    public String researchFormsAvg;
    public String currentFunFormsCount;
    public String funFormsAvg;
    public float balance;
    public int processFormCount;
    public Date lastTransactionTime;
    public String transactionsCount;
    public int charityTransactionsCount;
    public List<Form> listOfUsersForms;
    public List<UserPhoneDto> phones;
    public List<UserEmailDto> emails;

    public ProfileResponse(Builder builder) {
        this.userId = builder.userId;
        this.login = builder.login;
        this.debugRole = builder.debugRole;
        this.avatarUrl = builder.avatarUrl;
        this.currentResearchFormsCount = builder.currentResearchFormsCount;
        this.researchFormsAvg = builder.researchFormsAvg;
        this.currentFunFormsCount = builder.currentFunFormsCount;
        this.funFormsAvg = builder.funFormsAvg;
        this.phones = builder.phones;
        this.emails = builder.emails;
        this.balance = builder.balance;
        this.processFormCount = builder.processFormsCount;
        this.lastTransactionTime = builder.lastTransactionTime;
        this.transactionsCount = builder.transactionsCount;
        this.charityTransactionsCount = builder.charityTransactionsCount;
        this.listOfUsersForms = builder.listOfUsersForms;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof ProfileResponse)) {
            return false;
        }
        ProfileResponse profileResponse = (ProfileResponse) object;
        return Objects.equal(login, profileResponse.login)
                && Objects.equal(avatarUrl, profileResponse.avatarUrl)
                && Objects.equal(debugRole, profileResponse.debugRole)
                && Objects.equal(currentResearchFormsCount, profileResponse.currentResearchFormsCount)
                && Objects.equal(researchFormsAvg, profileResponse.researchFormsAvg)
                && Objects.equal(currentFunFormsCount, profileResponse.currentFunFormsCount)
                && Objects.equal(funFormsAvg, profileResponse.funFormsAvg)
                && Objects.equal(phones, profileResponse.phones)
                && Objects.equal(emails, profileResponse.emails)
                && Objects.equal(balance, profileResponse.balance)
                && Objects.equal(processFormCount, profileResponse.processFormCount)
                && Objects.equal(lastTransactionTime, profileResponse.lastTransactionTime)
                && Objects.equal(transactionsCount, profileResponse.transactionsCount)
                && Objects.equal(charityTransactionsCount, profileResponse.charityTransactionsCount)
                && Objects.equal(listOfUsersForms, profileResponse.listOfUsersForms);
    }

    public static class Builder {
        //required
        public String userId;
        public String login;

        //optional
        public int debugRole;
        public String avatarUrl;
        public String currentResearchFormsCount;
        public String researchFormsAvg;
        public String currentFunFormsCount;
        public String funFormsAvg;
        public float balance;
        public int processFormsCount;
        public Date lastTransactionTime;
        public String transactionsCount;
        public int charityTransactionsCount;
        public List<Form> listOfUsersForms;
        public List<UserPhoneDto> phones;
        public List<UserEmailDto> emails;

        public Builder(String login, String userId) {
            this.login = login;
            this.userId = userId;
        }

        public Builder debugRole(int debugRole) {
            this.debugRole = debugRole;
            return this;
        }

        public Builder avatar(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder currentResearchFormsCount(String currentResearchFormsCount) {
            this.currentResearchFormsCount = currentResearchFormsCount;
            return this;
        }

        public Builder researchFormsAvg(String researchFormsAvg) {
            this.researchFormsAvg = researchFormsAvg;
            return this;
        }

        public Builder currentFunFormsCount(String currentFunFormsCount) {
            this.currentFunFormsCount = currentFunFormsCount;
            return this;
        }

        public Builder email(List<UserEmailDto> emails) {
            this.emails = emails;
            return this;
        }

        public Builder phone(List<UserPhoneDto> phones) {
            this.phones = phones;
            return this;
        }

        public Builder funFormsAvg(String funFormsAvg) {
            this.funFormsAvg = funFormsAvg;
            return this;
        }


        public Builder balance(float balance) {
            this.balance = balance;
            return this;
        }

        public Builder processFormCount(int processFormsCount) {
            this.processFormsCount = processFormsCount;
            return this;
        }

        public Builder lastTransactionTime(long lastTransactionTime) {
            this.lastTransactionTime = new Date(lastTransactionTime);
            return this;
        }

        public Builder transactionsCount(String transactionsCount) {
            this.transactionsCount = transactionsCount;
            return this;
        }

        public Builder charityTransactionsCount(int charityTransactionsCount) {
            this.charityTransactionsCount = charityTransactionsCount;
            return this;
        }

        public Builder listOfUsersForms(List<Form> listOfUsersForms) {
            this.listOfUsersForms = listOfUsersForms;
            return this;
        }

        public ProfileResponse build() {
            return new ProfileResponse(this);
        }
    }
}