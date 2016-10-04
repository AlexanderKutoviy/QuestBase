package com.questbase.app.domain;

import com.questbase.app.dao.profile.phone.UserPhoneModel;
import com.questbase.app.utils.Objects;

public class UserPhoneDto {

    public String phone;
    public int isVerified;
    public int isPrimary;

    public UserPhoneDto() {
    }

    public UserPhoneDto(String phone, int isVerified, int isPrimary) {
        this.phone = phone;
        this.isVerified = isVerified;
        this.isPrimary = isPrimary;
    }

    public UserPhoneDto(UserPhoneModel userPhoneModel) {
        phone = userPhoneModel.phone;
        isVerified = userPhoneModel.verified;
        isPrimary = userPhoneModel.primary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, isVerified, isPrimary);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof UserPhoneDto)) {
            return false;
        }
        UserPhoneDto userPhoneDto = (UserPhoneDto) object;
        return Objects.equal(phone, userPhoneDto.phone)
                && Objects.equal(isVerified, userPhoneDto.isVerified)
                && Objects.equal(isPrimary, userPhoneDto.isPrimary);
    }
}