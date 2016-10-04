package com.questbase.app.controller.category;

import com.questbase.app.controller.Event;
import com.questbase.app.net.entity.Category;

import java.util.List;

import rx.Observable;

public interface CategoryController {

    void sync();

    Observable<Event<Category>> observe();

    void addCategory(Category category);

    void deleteCategory(Category category);

    void updateCategory(Category category);

    List<Category> readAll();
}