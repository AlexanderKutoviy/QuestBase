package com.questbase.app.dao.resource;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.domain.FormResource;

@Table(database = RespoDatabase.class)
public class ResourceModel extends RespoBaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column
    public String resId;

    @Column
    public String version;

    @Column
    public String filePath;

    @Column
    public Long formId;

    @Column
    public String state;

    public ResourceModel() {
    }

    public ResourceModel(FormResource formResource) {
        this.resId = formResource.resId;
        this.version = formResource.version;
        this.state = formResource.state.toString();
        this.formId = formResource.form.formId;
    }

    public FormResource toFormResource() {
        return new FormResource(this);
    }
}