package com.questbase.app.dao.category;

import com.questbase.app.net.entity.Category;

import java.util.List;

public interface CategoryDao {

    void create(Category category);

    List<Category> read();

    void update(Category category);

    void delete(Category category);
}