package com.questbase.app.dao.profile.email;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.domain.UserEmailDto;

@Table(database = RespoDatabase.class)
public class UserEmailModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public String address;

    @Column
    public int verified;

    @Column
    public int primary;

    @Column
    public String userId;

    public UserEmailModel() {
    }

    public UserEmailModel(UserEmailDto userEmailDto, String userId) {
        this.address = userEmailDto.address;
        this.verified = userEmailDto.isVerified;
        this.primary = userEmailDto.isPrimary;
        this.userId = userId;
    }

    public UserEmailDto toUserEmailDto() {
        return new UserEmailDto(this);
    }
}