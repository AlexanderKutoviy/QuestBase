package com.questbase.app.controller.font;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontController {

    public AssetManager assetManager;

    public FontController(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Typeface getMyriadProRegular() {
        return Typeface.createFromAsset(assetManager, "fonts/MyriadPro-Regular.ttf");
    }
}