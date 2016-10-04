package com.questbase.app.net.entity;

import com.questbase.app.dao.category.CategoryModel;
import com.questbase.app.domain.State;
import com.questbase.app.utils.Objects;

public class Category {

    public long categoryId;
    public long version;
    public String categoryName;
    public State state;

    public Category() {
    }

    public Category(CategoryModel categoryModel) {
        this(categoryModel.categoryId, categoryModel.version, categoryModel.categoryName, State.valueOf(categoryModel.state));
    }

    public Category(long formId, long version, String categoryName) {
        this.categoryId = formId;
        this.version = version;
        this.categoryName = categoryName;
    }

    public Category(long formId, long version, String categoryName, State state) {
        this.categoryId = formId;
        this.version = version;
        this.categoryName = categoryName;
        this.state = state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, version, categoryName);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Category)) {
            return false;
        }
        Category category = (Category) object;
        return Objects.equal(categoryId, category.categoryId)
                && Objects.equal(version, category.version)
                && Objects.equal(categoryName, category.categoryName);
    }

    @Override
    public String toString() {
        return "{ categoryId:" + categoryId +
                "; version: " + version +
                "; categoryName: " + categoryName +
                "; state: " + state + "; }";
    }
}
