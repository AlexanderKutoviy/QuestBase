package com.questbase.app.dao.personalresult;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.net.entity.PersonalResult;

@Table(database = RespoDatabase.class)
public class PersonalResultModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public long id;

    public PersonalResultModel() {
    }

    public PersonalResultModel(PersonalResult personalResult) {
        id = personalResult.id;
    }
}