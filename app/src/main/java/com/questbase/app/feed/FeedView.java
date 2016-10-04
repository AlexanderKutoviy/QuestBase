package com.questbase.app.feed;

import android.graphics.Bitmap;

import com.questbase.app.feed.feedcategories.CategoryType;
import com.questbase.app.feed.feedforms.FormItem;
import com.questbase.app.obsolete.ScriptManager;

import java.util.List;

interface FeedView {

    void setSearchTitle(String searchTitle);

    void setForms(List<FormItem> list);

    void setCategories(List<CategoryType> list);

    void setNothingToShow(boolean isVisible);

    void renderAvatar(Bitmap img);

    void setLoading(boolean isLoading);

    void showClearIcon(boolean visible);

    void showForm(FormItem form, ScriptManager.Model model);
}
