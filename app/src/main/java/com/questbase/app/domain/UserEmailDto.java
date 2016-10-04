package com.questbase.app.domain;

import com.questbase.app.dao.profile.email.UserEmailModel;
import com.questbase.app.utils.Objects;

public class UserEmailDto {

    public String address;
    public int isVerified;
    public int isPrimary;

    public UserEmailDto(String address, int isVerified, int isPrimary) {
        this.address = address;
        this.isVerified = isVerified;
        this.isPrimary = isPrimary;
    }

    public UserEmailDto(UserEmailModel userEmailModel) {
        address = userEmailModel.address;
        isVerified = userEmailModel.verified;
        isPrimary = userEmailModel.primary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, isVerified, isPrimary);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof UserEmailDto)) {
            return false;
        }
        UserEmailDto userEmailDto = (UserEmailDto) object;
        return Objects.equal(address, userEmailDto.address)
                && Objects.equal(isVerified, userEmailDto.isVerified)
                && Objects.equal(isPrimary, userEmailDto.isPrimary);
    }
}