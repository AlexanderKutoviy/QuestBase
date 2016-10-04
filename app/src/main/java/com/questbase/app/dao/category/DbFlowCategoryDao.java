package com.questbase.app.dao.category;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.net.entity.Category;

import java.util.List;

public class DbFlowCategoryDao implements CategoryDao {
    @Override
    public void create(Category category) {
        new CategoryModel(category).saveOnDuplicateUpdate();
    }

    @Override
    public List<Category> read() {
        return Stream.of(SQLite.select().from(CategoryModel.class).queryList())
                .map(CategoryModel::toCategory)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Category category) {
        create(category);
    }

    @Override
    public void delete(Category category) {
        new CategoryModel(category).delete();
    }
}
