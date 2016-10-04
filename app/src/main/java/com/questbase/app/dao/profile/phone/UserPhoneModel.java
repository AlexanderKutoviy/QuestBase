package com.questbase.app.dao.profile.phone;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.domain.UserPhoneDto;

@Table(database = RespoDatabase.class)
public class UserPhoneModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public String phone;

    @Column
    public int verified;

    @Column
    public int primary;

    @Column
    public String userId;

    public UserPhoneModel() {
    }

    public UserPhoneModel(UserPhoneDto userPhoneDto, String userId) {
        this.phone = userPhoneDto.phone;
        this.verified = userPhoneDto.isVerified;
        this.primary = userPhoneDto.isPrimary;
        this.userId = userId;
    }

    public UserPhoneDto toUserPhoneDto() {
        return new UserPhoneDto(this);
    }
}