package com.questbase.app.db;

import com.raizlabs.android.dbflow.structure.BaseModel;

public class RespoBaseModel extends BaseModel {

    public void saveOnDuplicateUpdate() {
        if (exists()) {
            update();
        } else {
            save();
        }
    }
}