package com.questbase.app.debugscreen;

import com.questbase.app.net.entity.Category;

public interface DebugView {

    void renderVersionNumber(String versionNumber);

    void renderSyncStartTime(String syncStartTime);

    void renderSyncEndTime(String syncEndTime);

    void renderIsSyncActive(boolean isSyncActive);

    void renderCategoriesAmount(long categoriesAmount);

    void renderFormsAmount(Category category, long formsAmount);

    void renderProfileInfo(String tokenId, String token, String userId);

    void cleanFormsAmount();
}