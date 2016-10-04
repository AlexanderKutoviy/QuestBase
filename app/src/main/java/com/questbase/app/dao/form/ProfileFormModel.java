package com.questbase.app.dao.form;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;

@Table(database = RespoDatabase.class)
public class ProfileFormModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public String userId;

    @PrimaryKey
    @Column
    public long formId;
}