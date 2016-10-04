package com.questbase.app.dao.form;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.dao.category.CategoryModel;
import com.questbase.app.dao.category.CategoryModel_Table;
import com.questbase.app.domain.Form;
import com.questbase.app.net.entity.Category;

import java.util.Collections;
import java.util.List;

public class DbFlowQuestDao implements QuestDao {

    @Override
    public void create(Form form) {
        new FormModel(form).saveOnDuplicateUpdate();
    }

    @Override
    public Optional<Form> read(Form form) {
        FormModel formModel = SQLite.select().from(FormModel.class)
                .where(FormModel_Table.id.eq(form.formId)).querySingle();
        return Optional.ofNullable(formModel).map(FormModel::toFormResponseWithResource);
    }

    @Override
    public List<Form> read(Category category) {
        CategoryModel categoryModel = SQLite.select().from(CategoryModel.class)
                .where(CategoryModel_Table.categoryId.eq(category.categoryId)).querySingle();
        List<FormModel> formModels = categoryModel != null ? categoryModel.getForms() : Collections.emptyList();
        return Stream.of(formModels).map(FormModel::toFormResponse).collect(Collectors.toList());
    }

    @Override
    public List<Form> readAll() {
        List<FormModel> list = SQLite.select()
                .from(FormModel.class)
                .queryList();
        return Stream.of(list).map(FormModel::toFormResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Form form) {
        new FormModel(form).delete();
    }

    @Override
    public void update(Form form) {
        SQLite.update(FormModel.class)
                .set(FormModel_Table.title.eq(form.title),
                        FormModel_Table.libVersion.eq(form.libVersion),
                        FormModel_Table.version.eq(form.version),
                        FormModel_Table.state.eq(form.state.toString()))
                .where(FormModel_Table.id.eq(form.formId))
                .async().execute();
    }

    @Override
    public void update(Form form, Category category) {
        SQLite.update(FormModel.class)
                .set(FormModel_Table.title.eq(form.title),
                        FormModel_Table.categoryId.eq(category.categoryId),
                        FormModel_Table.libVersion.eq(form.libVersion),
                        FormModel_Table.version.eq(form.version),
                        FormModel_Table.state.eq(form.state.toString()))
                .where(FormModel_Table.id.eq(form.formId))
                .async().execute();
    }
}
