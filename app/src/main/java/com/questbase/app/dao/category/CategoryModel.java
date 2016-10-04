package com.questbase.app.dao.category;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.dao.form.FormModel;
import com.questbase.app.dao.form.FormModel_Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.net.entity.Category;

import java.util.List;

@Table(database = RespoDatabase.class)
public class CategoryModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public long categoryId;

    @Column
    public long version;

    @Column
    public String categoryName;

    @Column
    public String state;

    public List<FormModel> forms;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "forms")
    public List<FormModel> getForms() {
        if (forms == null || forms.isEmpty()) {
            forms = SQLite.select()
                    .from(FormModel.class)
                    .where(FormModel_Table.categoryId.eq(categoryId))
                    .queryList();
        }
        return forms;
    }

    public CategoryModel() {
    }

    public CategoryModel(Category category) {
        categoryId = category.categoryId;
        version = category.version;
        categoryName = category.categoryName;
        state = category.state.toString();
    }

    public Category toCategory() {
        return new Category(this);
    }
}
