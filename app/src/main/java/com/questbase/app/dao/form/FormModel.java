package com.questbase.app.dao.form;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.dao.category.CategoryModel;
import com.questbase.app.dao.category.CategoryModel_Table;
import com.questbase.app.dao.resource.ResourceModel;
import com.questbase.app.dao.resource.ResourceModel_Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.State;
import com.questbase.app.net.entity.Category;

import java.util.List;

@Table(database = RespoDatabase.class)
public class FormModel extends RespoBaseModel {

    @Column
    @PrimaryKey
    public long id;

    @Column
    public String title;

    @Column
    public long categoryId;

    @Column
    public String libVersion;

    @Column
    public long version;

    @Column
    public String state;

    public List<ResourceModel> getResources() {
        return SQLite.select()
                .from(ResourceModel.class)
                .where(ResourceModel_Table.formId.eq(id))
                .queryList();
    }

    public FormModel() {
    }

    public FormModel(Form form) {
        this.id = form.formId;
        this.title = form.title;
        if (form.category != null) {
            this.categoryId = form.category.categoryId;
        } else {
            this.categoryId = 0;
        }
        this.libVersion = form.libVersion;
        this.version = form.version;
        if (form.state != null) {
            this.state = form.state.toString();
        }
    }

    public Form toFormResponse() {
        return new Form.Builder(id)
                .title(title)
                .libVersion(libVersion)
                .version(version)
                .category(getCategory())
                .state(State.valueOf(state))
                .build();
    }

    public Form toFormResponseWithResource() {
        return new Form.Builder(id)
                .title(title)
                .libVersion(libVersion)
                .version(version)
                .resources(Stream.of(getResources())
                        .map(ResourceModel::toFormResource)
                        .collect(Collectors.toList()))
                .category(getCategory())
                .state(State.valueOf(state))
                .build();
    }

    private Category getCategory() {
        return SQLite.select()
                .from(CategoryModel.class)
                .where(CategoryModel_Table.categoryId.eq(categoryId))
                .querySingle().toCategory();
    }
}
