package com.questbase.app.feed.feedcategories;

import com.questbase.app.R;

public enum CategoryType {

    AUTO(1, R.drawable.car_icon_enabled, R.drawable.car_icon_pressed, "Авто та техніка"),
    HEALTH(2, R.drawable.hospitals_icon_enabled, R.drawable.hospitals_icon_pressed, "Здоров'я"),
    BEAUTY(3, R.drawable.cosmetics_icon_enabled, R.drawable.cosmetics_icon_pressed, "Краса"),
    POLICY(4, R.drawable.politics_icon_enabled, R.drawable.politics_icon_pressed, "Політика"),
    KNOWLEDGE(5, R.drawable.knowledge_icon_enabled, R.drawable.knowledge_icon_pressed, "Знання"),
    PSYCHOLOGY(6, R.drawable.psychology_icon_enabled, R.drawable.psychology_icon_pressed, "Психологія"),
    ENTERTAINMENT(7, R.drawable.entertainment_icon_enabled, R.drawable.entertainment_icon_pressed, "Розваги"),
    SHOPPING(8, R.drawable.clothes_icon_enabled, R.drawable.clothes_icon_pressed, "Шопінг та стиль життя");

    private long categoryId;
    private int iconId;
    private int pressedIconId;
    private String name;

    CategoryType(long categoryId, int iconId, int pressedIconId, String name) {
        this.categoryId = categoryId;
        this.iconId = iconId;
        this.pressedIconId = pressedIconId;
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getPressedIconId() {
        return pressedIconId;
    }

    public String getName() {
        return name;
    }
}
