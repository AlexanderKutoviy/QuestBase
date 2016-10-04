package com.questbase.app.feed.feedforms;

import com.questbase.app.feed.feedcategories.CategoryType;
import com.questbase.app.net.entity.Category;

import java.util.NoSuchElementException;

public class FormItem {
    private long formId;
    private long formVersion;
    private String title;
    private String libVersion;
    private CategoryType categoryType;

    public FormItem(long formId, Category category) {
        this.formId = formId;
        this.categoryType = categoryTypeFromCategory(category);
    }

    public FormItem(long formId, long formVersion, String title, String libVersion, Category category) {
        this.formId = formId;
        this.formVersion = formVersion;
        this.title = title;
        this.libVersion = libVersion;
        this.categoryType = categoryTypeFromCategory(category);
    }

    public String getTitle() {
        return title;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public long getFormId() {
        return formId;
    }

    public long getFormVersion() {
        return formVersion;
    }

    public String getLibVersion() {
        return libVersion;
    }

    private CategoryType categoryTypeFromCategory(Category category) {
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.getCategoryId() == category.categoryId) {
                return categoryType;
            }
        }
        throw new NoSuchElementException("This category " + category + " doesn't exist!");
    }
}
